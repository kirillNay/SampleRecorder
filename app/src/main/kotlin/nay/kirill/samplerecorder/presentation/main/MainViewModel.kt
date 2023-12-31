package nay.kirill.samplerecorder.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.usecase.GetSamplesUseCase
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.usecase.ClearLayersUseCase
import nay.kirill.samplerecorder.domain.usecase.CreateLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.CreateVoiceSampleUseCase
import nay.kirill.samplerecorder.domain.usecase.ObserveLayersUseCase
import nay.kirill.samplerecorder.domain.usecase.RemoveLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.SaveLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.SetPlayingLayerUseCase

class MainViewModel(
    private val stateConverter: MainStateConverter,
    private val player: Player,
    private val saveLayerUseCase: SaveLayerUseCase,
    private val createLayerUseCase: CreateLayerUseCase,
    private val removeLayerUseCase: RemoveLayerUseCase,
    private val setPlayingLayerUseCase: SetPlayingLayerUseCase,
    private val createVoiceSampleUseCase: CreateVoiceSampleUseCase,
    private val clearLayersUseCase: ClearLayersUseCase,
    private val artManager: ArtAnimationManager,
    observeLayersUseCase: ObserveLayersUseCase,
    getSamplesUseCase: GetSamplesUseCase,
) : ViewModel() {

    private val _eventsFlow = MutableSharedFlow<MainEvent>()
    val eventsFlow = _eventsFlow.asSharedFlow()

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

    private var setAudioParamsJob: Job? = null

    private var finalRecordProgressJob: Job? = null

    private var artsStateObserveJob: Job? = null

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
            is MainIntent.FinalRecord.Share -> reduceShareFile()
            is MainIntent.FinalRecord.Reset -> reduceReset()
            is MainIntent.FinalRecord.Open -> reduceOpenFile()
            is MainIntent.Lifecycle.OnPause -> pauseAllSamples()
            is MainIntent.Lifecycle.OnResume -> resumeAllSamples()
            is MainIntent.Lifecycle.OnDestroy -> reduceOnDestroy()
            is MainIntent.FinalRecord.Visualising -> reduceOpenVisualising()
            is MainIntent.Visualising.Back -> reduceBackFromVisualising()
            is MainIntent.Visualising.NameEdit -> reduceNameEdit(intent)
            is MainIntent.Visualising.Play -> reduceFinalRecordPlay()
            is MainIntent.Visualising.ToEnd -> reduceFinalRecordToEnd()
            is MainIntent.Visualising.ToStart -> reduceFinalRecordToStart()
        }
    }

    private fun reduceFinalRecordToEnd() {
        player.setFinalRecordProgress(1F)
    }

    private fun reduceFinalRecordToStart() {
        player.setFinalRecordProgress(0F)
    }

    private fun reduceFinalRecordPlay() {
        val vState = requireNotNull(state.visualisingState)

        artManager.isPlaying = !vState.isPlaying
        state = if (vState.isPlaying) {
            player.pauseFinalRecord()

            state.copy(visualisingState = vState.copy(isPlaying = false))
        } else {
            player.playFinalRecord()
            state.copy(visualisingState = vState.copy(isPlaying = true))
        }

    }

    private fun reduceNameEdit(intent: MainIntent.Visualising.NameEdit) {
        val vState = requireNotNull(state.visualisingState)

        state = state.copy(
            visualisingState = vState.copy(name = intent.name)
        )
    }

    private fun reduceBackFromVisualising() {
        artsStateObserveJob?.cancel()
        player.pauseFinalRecord()
        state = state.copy(visualisingState = null)
    }

    private fun reduceOpenVisualising() {
        val record = (state.finalRecordState as FinalRecordState.Complete).data

        player.createFinalRecordPlayer()

        artsStateObserveJob?.cancel()
        artsStateObserveJob = viewModelScope.launch {
            artManager.observeArtsState().collect {
                val vState = state.visualisingState ?: return@collect

                state = state.copy(
                    visualisingState = vState.copy(
                        arts = it
                    )
                )
            }
        }

        finalRecordProgressJob?.cancel()
        finalRecordProgressJob = viewModelScope.launch {
            var counter: Int = 0
            player.observeFinalProgress().collect { progress ->
                val vState = state.visualisingState ?: return@collect

                if (progress == 1F) { artManager.isPlaying = false }

                record.recordData.getOrNull((record.recordData.size * progress).toInt())?.let {
                    artManager.speed = 1 + kotlin.math.abs(it) * 50
                }

                state = state.copy(
                    visualisingState = vState.copy(
                        progress = progress,
                        isPlaying = if (progress == 1F) false else vState.isPlaying
                    )
                )
            }
        }

        state = state.copy(
            visualisingState = VisualisingState(
                audioDuration = record.duration.toInt(),
                name = record.fileName,
                progress = 0F,
                isPlaying = false,
                arts = artManager.artsScaleState
            )
        )
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
        when (state.finalRecordState) {
            FinalRecordState.Process -> {
                state = state.copy(
                    finalRecordState = FinalRecordState.Saving
                )
                pauseAllSamples()

                viewModelScope.launch {
                    player.stopRecording()
                        .onSuccess {
                            state = state.copy(
                                finalRecordState = FinalRecordState.Complete(it),
                            )
                        }
                        .onFailure(::onFailure)

                    clearLayersUseCase()
                }
            }

            else -> {
                state = state.copy(
                    finalRecordState = FinalRecordState.Process
                )
                player.startRecording()
            }
        }
    }

    private fun reduceOnRecord() {
        when {
            state.isVoiceRecording -> {
                val sample = createVoiceSampleUseCase()
                val layer = state.currentLayer?.copy(sample = sample)

                state = state.copy(
                    isVoiceRecording = false,
                )
                layer?.let { saveLayerUseCase(it) }
                player.stopVoiceRecording(sample.id)
            }

            else -> {
                state = state.copy(isVoiceRecording = true)
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
        state.layers.find { it.id == intent.id }?.sample?.let { player.stop(it.id) }
        removeLayerUseCase(intent.id)
    }

    private fun resetStateWithLayer(layer: Layer) {
        state = MainState(
            samples = state.samples,
            currentLayerId = layer.id,
            layers = state.layers,
            initialSpeedScale = (MAX_SPEED_VALUE - layer.speed) / (MAX_SPEED_VALUE - MIN_SPEED_VALUE),
            initialVolumeScale = (MAX_VOLUME_VALUE - layer.volume) / (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE)
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
            withContext(Dispatchers.IO) {
                player.getAmplitude(sample.id)
                    .onSuccess { state = state.copy(amplitude = it) }
                    .onFailure { state = state.copy(amplitude = null) }
            }
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

    private fun reduceShareFile() {
        viewModelScope.launch {
            val fileDirectory = (state.finalRecordState as FinalRecordState.Complete).data.fileDir
            _eventsFlow.emit(
                MainEvent.ShareFile(requireNotNull(fileDirectory) { "Field fileDirectory in state is null!" })
            )
        }
    }

    private fun reduceOpenFile() {
        viewModelScope.launch {
            val fileDirectory = (state.finalRecordState as FinalRecordState.Complete).data.fileDir
            _eventsFlow.emit(
                MainEvent.OpenFile(requireNotNull(fileDirectory) { "Field fileDirectory in state is null!" })
            )
        }
    }

    private fun reduceReset() {
        player.releasePlayer()
        state = MainState(
            samples = state.samples,
            currentLayerId = createLayerUseCase().id
        )
        player.create(state.samples)
    }

    private fun setupAudioParams() {
        state.currentLayer?.sample?.id?.let { sampleId ->
            val layer = state.currentLayer ?: return

            setAudioParamsJob?.cancel()
            setAudioParamsJob = viewModelScope.launch {
                delay(100)
                player.setSpeed(sampleId, layer.speed)
                player.setVolume(sampleId, layer.volume)
            }
        }
    }

    private fun stopCurrentSample() {
        val sample = state.currentLayer?.sample ?: return

        player.stop(sample.id)
    }

    private fun onFailure(exception: Throwable) {
        state = MainState(
            samples = state.samples,
            currentLayerId = 0,
            exception = exception
        )
        viewModelScope.launch {
            _eventsFlow.emit(MainEvent.FailureToast(exception.message))
        }
    }

    private fun pauseAllSamples() {
        state.layers.filter { it.isPlaying }.forEach {
            it.sample?.id?.let(player::pause)
        }
    }

    private fun resumeAllSamples() {
        state.layers.filter { it.isPlaying }.forEach {
            it.sample?.id?.let(player::resume)
        }
    }

    private fun reduceOnDestroy() {
        player.releasePlayer()
    }

}