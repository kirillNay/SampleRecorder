package nay.kirill.samplerecorder.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainState.initial)
    val state = _state.asStateFlow()

    fun accept(intent: MainIntent) {
        when (intent) {
            is MainIntent.SelectSample.Default -> reduceDefaultSample(intent)
            else -> Unit
        }
    }

    private fun reduceDefaultSample(intent: MainIntent.SelectSample.Default) {

    }

}