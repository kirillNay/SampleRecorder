package nay.kirill.samplerecorder.presentation

import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.presentation.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooserUIState

data class MainState(
    val samples: List<Sample>,
    val selectedSampleId: Int? = null,
    val expandedType: SampleType? = null,
    val isPlaying: Boolean = false,
    val amplitude: List<Float>? = null,
    val progress: Float
) {

    val selectedSample: Sample? get() = samples.find { it.id == selectedSampleId }

}

data class MainUIState(
    val chooserState: SampleChooserUIState,
    val playerControllerState: PlayerControllerState,
    val timeline: PlayerTimelineState = PlayerTimelineState.Empty
)