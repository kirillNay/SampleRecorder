package nay.kirill.samplerecorder.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nay.kirill.samplerecorder.domain.GetSamplesUseCase
import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.Sample

class MainViewModel(
    private val stateConverter: MainStateConverter,
    private val player: Player,
    getSamplesUseCase: GetSamplesUseCase
) : ViewModel() {

    private var state: MainState = MainState(
        samples = getSamplesUseCase(),
        progress = 0F
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

    fun accept(intent: MainIntent) {
        when (intent) {
            is MainIntent.SelectSample.Default -> reduceDefaultSample(intent)
            is MainIntent.SelectSample.Expand -> reduceExpandSample(intent)
            is MainIntent.SelectSample.Sample -> reduceSelectSample(intent)
            is MainIntent.Player.OnPlayButton -> reduceOnPlayButton()
            else -> Unit
        }
    }

    private fun reduceDefaultSample(intent: MainIntent.SelectSample.Default) {
        val sample = state.samples.first { it.type == intent.type }

        if (sample.type == state.selectedSample?.type) {
            if (!player.isPlaying) player.playOnce()
            return
        }

        state = state.copy(
            selectedSampleId = sample.id,
            expandedType = null,
            progress = 0F
        )
        onSampleSelected(sample)
    }

    private fun reduceExpandSample(intent: MainIntent.SelectSample.Expand) {
        state = state.copy(
            expandedType = intent.type
        )
    }

    private fun reduceSelectSample(intent: MainIntent.SelectSample.Sample) {
        state = state.copy(
            selectedSampleId = intent.id,
            expandedType = null,
            progress = 0F
        )
        state.samples.find { it.id == intent.id }?.let(::onSampleSelected)
    }

    private fun onSampleSelected(sample: Sample) {
        player.create(sample.resourceId)
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

    companion object {

        const val SELECTED_SAMPLE_PLAY_DELAY = 200L

    }

}