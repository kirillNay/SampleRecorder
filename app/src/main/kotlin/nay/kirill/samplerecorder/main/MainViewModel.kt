package nay.kirill.samplerecorder.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import nay.kirill.samplerecorder.domain.GetSamplesUseCase

class MainViewModel(
    private val stateConverter: MainStateConverter,
    getSamplesUseCase: GetSamplesUseCase
) : ViewModel() {

    private var state: MainState = MainState(
        samples = getSamplesUseCase()
    )
        private set(value) {
            if (value == field) return

            _uiState.value = stateConverter(value)
            field = state
        }

    private val _uiState = MutableStateFlow(stateConverter(state))
    val uiState = _uiState.asStateFlow()

    fun accept(intent: MainIntent) {
        when (intent) {
            is MainIntent.SelectSample.Default -> reduceDefaultSample(intent)
            is MainIntent.SelectSample.Expand -> reduceExpandSample(intent)
            is MainIntent.SelectSample.Sample -> reduceSelectSample(intent)
        }
    }

    private fun reduceDefaultSample(intent: MainIntent.SelectSample.Default) {
        state = state.copy(
            selectedSampleId = state.samples.first { it.type == intent.type }.id
        )
    }

    private fun reduceExpandSample(intent: MainIntent.SelectSample.Expand) {
        state = state.copy(
            expandedType = intent.type
        )
    }

    private fun reduceSelectSample(intent: MainIntent.SelectSample.Sample) {
        state = state.copy(
            selectedSampleId = intent.id,
            expandedType = null
        )
    }

}