package nay.kirill.samplerecorder.presentation

import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.presentation.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooserUIState

data class MainState(
    val samples: List<Sample>,
    val selectedSampleId: Int? = null,
    val expandedType: SampleType? = null,
    val isPlaying: Boolean = false,
    val amplitude: List<Float>? = null,
    val progress: Float,
    val initialSpeed: Float,
    val initialVolume: Float,
    val speed: Float = 1F,
    val volume: Float = 1F,
    val duration: Int = 0
) {

    val selectedSample: Sample? get() = samples.find { it.id == selectedSampleId }

}

sealed interface MainUIState {

    val chooserState: SampleChooserUIState

    data class Empty(
        override val chooserState: SampleChooserUIState
    ) : MainUIState

    data class Sampling(
        override val chooserState: SampleChooserUIState,
        val playerControllerState: PlayerControllerState,
        val timeline: PlayerTimelineState,
        val audioControllerState: AudioControllerState
    ) : MainUIState

}