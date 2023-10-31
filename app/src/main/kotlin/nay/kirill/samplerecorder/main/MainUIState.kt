package nay.kirill.samplerecorder.main

import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooserUIState

data class MainState(
    val samples: List<Sample>,
    val selectedSampleId: Int? = null
)

data class MainUIState(
    val chooserState: SampleChooserUIState
)