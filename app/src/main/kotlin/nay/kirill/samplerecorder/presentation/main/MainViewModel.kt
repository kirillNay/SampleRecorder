package nay.kirill.samplerecorder.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.usecase.GetSamplesUseCase
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.usecase.CreateLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.CreateVoiceSample
import nay.kirill.samplerecorder.domain.usecase.ObserveLayersUseCase
import nay.kirill.samplerecorder.domain.usecase.RemoveLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.SaveLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.SetPlayingLayerUseCase
import java.io.File

class MainViewModel(
    private val stateConverter: MainStateConverter,
    private val player: Player,
    private val saveLayerUseCase: SaveLayerUseCase,
    private val createLayerUseCase: CreateLayerUseCase,
    private val removeLayerUseCase: RemoveLayerUseCase,
    private val setPlayingLayerUseCase: SetPlayingLayerUseCase,
    private val createVoiceSample: CreateVoiceSample,
    observeLayersUseCase: ObserveLayersUseCase,
    getSamplesUseCase: GetSamplesUseCase,
    private val context: Context
) : ViewModel() {

    val startIntent: MutableSharedFlow<Intent> = MutableSharedFlow()

    private val layersFlow = observeLayersUseCase()

    private var state: MainState = MainState(
        samples = getSamplesUseCase(),
        currentLayerId = createLayerUseCase().id
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
            is MainIntent.PlayerController.OnRecord -> reduceOnRecord()
            is MainIntent.Layers.SelectLayer -> reduceSelectLayer(intent)
            is MainIntent.Layers.CreateNew -> reduceNewLayer()
            is MainIntent.Layers.RemoveLayer -> reduceRemoveLayer(intent)
            is MainIntent.Layers.SetPlaying -> reduceSetLayerPlaying(intent)
            is MainIntent.PlayerController.OnFinalRecord -> reduceOnFinalRecord()
        }
    }

    private fun reduceSetLayerPlaying(intent: MainIntent.Layers.SetPlaying) {
        setPlayingLayerUseCase(intent.id, intent.isPlaying)

        val selectedSampleId = state.layers.find { it.id == intent.id }?.sample?.id ?: return
        if (intent.isPlaying) {
            player.resume(selectedSampleId)
        } else {
            player.pause(selectedSampleId)
        }
    }

    private fun reduceOnFinalRecord() {
        when {
            state.isFinalRecording -> {
                state = state.copy(
                    isFinalRecording = false
                )
                player.stopRecording()

                val sharedIntent = Intent().apply {
                    action = Intent.ACTION_SEND_MULTIPLE
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListOf(Uri.fromFile(File("/storage/emulated/0/Music/final.wav"))))
                    type = "audio/*"
                }
                startIntent.tryEmit(sharedIntent)
            }
            else -> {
                state = state.copy(
                    isFinalRecording = true
                )
                player.startRecording()
            }
        }
    }

    private fun reduceOnRecord() {
        when {
            state.isRecording -> {
                val sample = createVoiceSample()
                val layer = state.currentLayer?.copy(sample = sample)

                state = state.copy(
                    isRecording = false,
                )
                layer?.let { saveLayerUseCase(it) }
                player.stopVoiceRecording(sample.id)
            }
            else -> {
                state = state.copy(isRecording = true)
                player.startVoiceRecording()
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
        state.layers.find { it.id == intent.id }?.sample?.let { player.stop(it.id) }
    }

    private fun resetStateWithLayer(layer: Layer) {
        state = MainState(
            samples = state.samples,
            currentLayerId = layer.id,
            layers = state.layers
        )
        layer.sample?.let { onSampleSelected(it, layer.isPlaying) }
    }

    private fun reduceOpenLayerModal(intent: MainIntent.PlayerController.LayersModal) {
        state = state.copy(isLayersOpen = intent.open)
    }

    private fun reduceDefaultSample(intent: MainIntent.SelectSample.Default) {
        val sample = state.samples.first { it.type == intent.type }

        if (sample.type == state.selectedSample?.type) {
            player.playLoop(sample.id)
            return
        }

        onSampleSelected(sample)
        state.currentLayer?.let { saveLayerUseCase(it.copy(sample = sample)) }

        state = state.copy(
            expandedType = null,
            progress = 0F,
            duration = player.getDuration(sample.id)
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
        state.currentLayer?.let { saveLayerUseCase(it.copy(sample = sample)) }

        state = state.copy(
            expandedType = null,
            progress = 0F,
            duration = player.getDuration(sample.id)
        )
    }

    private fun onSampleSelected(sample: Sample, play: Boolean = true) {
        if (play) {
            stopCurrentSample()
            player.playLoop(sample.id)
            setPlayingLayerUseCase(state.currentLayerId, true)
        }
        initPlayerObserver(sample)

        viewModelScope.launch {
            player.getAmplitude(sample.id)
                .onSuccess { state = state.copy(amplitude = it) }
                .onFailure { state = state.copy(amplitude = null) }
        }
    }

    private fun reduceOnPlayButton() {
        val id = state.currentLayer?.sample?.id ?: return

        if (state.isPlaying) {
            player.pause(id)
        } else {
            player.resume(id)
        }
        setPlayingLayerUseCase(state.currentLayerId, !state.isPlaying)
    }

    private fun reduceNewAudioParams(intent: MainIntent.AudioParams.NewParams) {
        val speed = MAX_SPEED_VALUE - (MAX_SPEED_VALUE - MIN_SPEED_VALUE) * intent.speed
        val volume = MAX_VOLUME_VALUE - (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE) * intent.volume

        state.currentLayer?.let { saveLayerUseCase(it.copy(speed = speed, volume = volume)) }

        setupAudioParams()
    }

    private fun initPlayerObserver(sample: Sample) {
        playerProgressObserverJob?.cancel()
        playerProgressObserverJob = viewModelScope.launch {
             player.observeProgress(sample.id).collect { progress ->
                 state = state.copy(progress = progress)
             }
        }
    }

    private fun reduceSeekPlayer(intent: MainIntent.PlayerController.Seek) {
        state.currentLayer?.sample?.id?.let {
            player.seekTo(it, intent.position)
        }
    }

    private fun setupAudioParams() {
        state.currentLayer?.sample?.id?.let { sampleId ->
            val layer = state.currentLayer ?: return

            player.setSpeed(sampleId, layer.speed)
            player.setVolume(sampleId, layer.volume)
        }
    }

    private fun stopCurrentSample() {
        val sample = state.currentLayer?.sample ?: return

        player.stop(sample.id)
    }

}