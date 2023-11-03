package nay.kirill.samplerecorder.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nay.kirill.samplerecorder.domain.usecase.GetSamplesUseCase
import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.usecase.CreateLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.ObserveLayersUseCase
import nay.kirill.samplerecorder.domain.usecase.SaveLayerUseCase
import kotlin.math.ln

class MainViewModel(
    private val stateConverter: MainStateConverter,
    private val player: Player,
    private val getSamplesUseCase: GetSamplesUseCase,
    private val saveLayerUseCase: SaveLayerUseCase,
    private val createLayerUseCase: CreateLayerUseCase,
    private val observeLayersUseCase: ObserveLayersUseCase
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

    private var playerStateObserverJob: Job? = null
    private var playerProgressObserverJob: Job? = null

    init {
        viewModelScope.launch {
            layersFlow.collect {
                state = state.copy(layers = it)
            }
        }
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
            is MainIntent.Layers.SelectLayer -> reduceSelectLayer(intent)
            is MainIntent.Layers.CreateNew -> reduceNewLayer()
        }
    }

    private fun reduceSelectLayer(intent: MainIntent.Layers.SelectLayer) {
        state.layers.find { it.id == intent.id }?.let(::resetStateWithLayer)
    }

    private fun reduceNewLayer() {
        val layer = createLayerUseCase()
        resetStateWithLayer(layer)
    }

    private fun resetStateWithLayer(layer: Layer) {
        player.stopAndRelease()
        state = MainState(
            samples = state.samples,
            currentLayer = layer,
            layers = state.layers
        )
        layer.sample?.let(::onSampleSelected)
    }

    private fun reduceOpenLayerModal(intent: MainIntent.PlayerController.LayersModal) {
        state = state.copy(isLayersOpen = intent.open)
    }

    private fun reduceDefaultSample(intent: MainIntent.SelectSample.Default) {
        val sample = state.samples.first { it.type == intent.type }

        if (sample.type == state.selectedSample?.type) {
            if (!player.isPlaying) player.playOnce()
            return
        }

        onSampleSelected(sample)
        saveLayerUseCase(state.currentLayer.copy(sample = sample))

        state = state.copy(
            currentLayer = state.currentLayer.copy(sample = sample),
            expandedType = null,
            progress = 0F,
            duration = player.duration
        )
    }

    private fun reduceExpandSample(intent: MainIntent.SelectSample.Expand) {
//        val sample = state.samples.first { it.type == intent.type }
//        if (state.expandedType != intent.type) onSampleSelected(sample)
        state = state.copy(
            expandedType = intent.type,
            currentLayer = state.currentLayer,
            duration = player.duration
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
            duration = player.duration
        )
    }

    private fun onSampleSelected(sample: Sample) {
        player.create(sample.resourceId, state.currentLayer.speed, state.currentLayer.volume)
        viewModelScope.launch {
            delay(SELECTED_SAMPLE_PLAY_DELAY)
            player.playOnce()
        }
        initPlayerObserver()

        viewModelScope.launch {
            player.getAmplitude()
                .onSuccess { state = state.copy(amplitude = it) }
                .onFailure { state = state.copy(amplitude = null) }

        }
    }

    private fun reduceOnPlayButton() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.playLoop()
            setupAudioParams()
        }
    }

    private fun reduceNewAudioParams(intent: MainIntent.AudioParams.NewParams) {
        val speed = MIN_SPEED_VALUE + (MAX_SPEED_VALUE - MIN_SPEED_VALUE) * intent.speed
        val volume = ln(MAX_VOLUME_VALUE - (MIN_VOLUME_VALUE + (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE) * intent.volume)) / ln(MAX_VOLUME_VALUE)

        state = state.copy(
            currentLayer = state.currentLayer.copy(speed = speed, volume = volume)
        )
        saveLayerUseCase(state.currentLayer)

        if (player.isPlaying) {
            setupAudioParams()
        }
    }

    private fun initPlayerObserver() {
        playerStateObserverJob?.cancel()
        playerStateObserverJob = viewModelScope.launch {
            player.state.collect { playerState ->
                when (playerState) {
                    is Player.State.Play -> state = state.copy(isPlaying = true)
                    is Player.State.Pause, is Player.State.Completed -> state = state.copy(isPlaying = false)
                    else -> Unit
                }
            }
        }

        playerProgressObserverJob?.cancel()
        playerProgressObserverJob = viewModelScope.launch {
            player.progress.collect { state = state.copy(progress = it) }
        }
    }

    private fun reduceSeekPlayer(intent: MainIntent.PlayerController.Seek) {
        player.seekTo(intent.position)
    }

    private fun setupAudioParams() {
        player.setSpeed(state.currentLayer.speed)
        player.setVolume(state.currentLayer.volume)
    }

    companion object {

        private const val SELECTED_SAMPLE_PLAY_DELAY = 200L

    }

}