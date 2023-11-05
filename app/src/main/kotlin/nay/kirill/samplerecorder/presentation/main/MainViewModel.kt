package nay.kirill.samplerecorder.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.usecase.GetSamplesUseCase
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.usecase.CreateLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.ObserveLayersUseCase
import nay.kirill.samplerecorder.domain.usecase.RemoveLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.SaveLayerUseCase

class MainViewModel(
    private val stateConverter: MainStateConverter,
    private val player: Player,
    private val saveLayerUseCase: SaveLayerUseCase,
    private val createLayerUseCase: CreateLayerUseCase,
    private val removeLayerUseCase: RemoveLayerUseCase,
    observeLayersUseCase: ObserveLayersUseCase,
    getSamplesUseCase: GetSamplesUseCase
) : ViewModel() {

    private val layersFlow = observeLayersUseCase()

    private var state: MainState = MainState(
        samples = getSamplesUseCase(),
        currentLayer = createLayerUseCase()
    )
        private set(value) {
            if (value == field) return

            _uiState.value = stateConverter(value)
            field = value
        }

    private val _uiState = MutableStateFlow(stateConverter(state))
    val uiState = _uiState.asStateFlow()

    private var playerProgressObserverJob: Job? = null

    init {
        viewModelScope.launch {
            layersFlow.collect {
                state = state.copy(layers = it)
            }
        }

        player.create(state.samples)
    }

    fun accept(intent: MainIntent) {
        when (intent) {
            is MainIntent.SelectSample.Default -> reduceDefaultSample(intent)
            is MainIntent.SelectSample.Expand -> reduceExpandSample(intent)
            is MainIntent.SelectSample.Sample -> reduceSelectSample(intent)
            is MainIntent.PlayerController.OnPlayButton -> reduceOnPlayButton()
            is MainIntent.AudioParams.NewParams -> reduceNewAudioParams(intent)
            is MainIntent.PlayerController.Seek -> reduceSeekPlayer(intent)
            is MainIntent.PlayerController.LayersModal -> reduceOpenLayerModal(intent)
            is MainIntent.PlayerController.OnRecord -> reduceOnLayer()
            is MainIntent.Layers.SelectLayer -> reduceSelectLayer(intent)
            is MainIntent.Layers.CreateNew -> reduceNewLayer()
            is MainIntent.Layers.RemoveLayer -> reduceRemoveLayer(intent)
        }
    }

    private fun reduceOnLayer() {
        when {
            state.isRecording -> {
                // TODO update layer
                state = state.copy(isRecording = false)
            }
            else -> {
                state = state.copy(isRecording = true)
                // player.pause()
            }
        }

    }

    private fun reduceSelectLayer(intent: MainIntent.Layers.SelectLayer) {
        state.layers.find { it.id == intent.id }?.let(::resetStateWithLayer)
    }

    private fun reduceNewLayer() {
        val layer = createLayerUseCase()
        resetStateWithLayer(layer)
    }

    private fun reduceRemoveLayer(intent: MainIntent.Layers.RemoveLayer) {
        removeLayerUseCase(intent.id)
    }

    private fun resetStateWithLayer(layer: Layer) {
        state = MainState(
            samples = state.samples,
            currentLayer = layer,
            layers = state.layers,
            isPlaying = true
        )
        layer.sample?.let(::onSampleSelected)
    }

    private fun reduceOpenLayerModal(intent: MainIntent.PlayerController.LayersModal) {
        state = state.copy(isLayersOpen = intent.open)
    }

    private fun reduceDefaultSample(intent: MainIntent.SelectSample.Default) {
        val sample = state.samples.first { it.type == intent.type }

        if (sample.type == state.selectedSample?.type) {
            player.playOnce(sample.id)
            return
        }

        onSampleSelected(sample)
        saveLayerUseCase(state.currentLayer.copy(sample = sample))

        state = state.copy(
            currentLayer = state.currentLayer.copy(sample = sample),
            expandedType = null,
            progress = 0F,
            duration = player.getDuration(sample.id),
            isPlaying = true
        )
    }

    private fun reduceExpandSample(intent: MainIntent.SelectSample.Expand) {
        state = state.copy(
            expandedType = intent.type
        )
    }

    private fun reduceSelectSample(intent: MainIntent.SelectSample.Sample) {
        val sample = state.samples.find { it.id == intent.id } ?: return
        onSampleSelected(sample)
        saveLayerUseCase(state.currentLayer.copy(sample = sample))

        state = state.copy(
            currentLayer = state.currentLayer.copy(sample = sample),
            expandedType = null,
            progress = 0F,
            duration = player.getDuration(sample.id),
            isPlaying = true
        )
    }

    private fun onSampleSelected(sample: Sample) {
        stopCurrentSample()
        player.playOnce(sample.id)
        initPlayerObserver(sample)

        viewModelScope.launch {
            player.getAmplitude(sample.id)
                .onSuccess { state = state.copy(amplitude = it) }
                .onFailure { state = state.copy(amplitude = null) }
        }
    }

    private fun reduceOnPlayButton() {
        val id = state.currentLayer.sample?.id ?: return
        state = state.copy(isPlaying = !state.isPlaying)
        if (state.isPlaying) {
            player.resume(id, true)
        } else {
            player.pause(id)
        }
    }

    private fun reduceNewAudioParams(intent: MainIntent.AudioParams.NewParams) {
        val speed = MAX_SPEED_VALUE - (MAX_SPEED_VALUE - MIN_SPEED_VALUE) * intent.speed
        val volume = MAX_VOLUME_VALUE - (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE) * intent.volume

        state = state.copy(
            currentLayer = state.currentLayer.copy(speed = speed, volume = volume)
        )
        saveLayerUseCase(state.currentLayer)

        setupAudioParams()
    }

    private fun initPlayerObserver(sample: Sample) {
        playerProgressObserverJob?.cancel()
        playerProgressObserverJob = viewModelScope.launch {
             player.observeProgress(sample.id).collect { progress ->
                 state = if (progress >= 1F) {
                     state.copy(progress = progress, isPlaying = false)
                 } else {
                     state.copy(progress = progress)
                 }

             }
        }
    }

    private fun reduceSeekPlayer(intent: MainIntent.PlayerController.Seek) {
        state.currentLayer.sample?.id?.let {
            player.seekTo(it, intent.position)
        }
    }

    private fun setupAudioParams() {
        state.currentLayer.sample?.id?.let { sampleId ->
            player.setSpeed(sampleId, state.currentLayer.speed)
            player.setVolume(sampleId, state.currentLayer.volume)
        }
    }

    private fun stopCurrentSample() {
        val sample = state.currentLayer.sample ?: return

        player.stop(sample.id)
    }

}