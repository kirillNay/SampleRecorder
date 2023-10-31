package nay.kirill.samplerecorder.main

import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.main.playerController.PlayerControllerState
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooserUIState

data class MainState(
    val samples: List<Sample>,
    val selectedSampleId: Int? = null,
    val expandedType: SampleType? = null,
    val isPlaying: Boolean = false
) {

    val selectedSample: Sample? get() = samples.find { it.id == selectedSampleId }

}

data class MainUIState(
    val chooserState: SampleChooserUIState,
    val playerControllerState: PlayerControllerState
)