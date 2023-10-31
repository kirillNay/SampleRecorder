package nay.kirill.samplerecorder.main.sampleChooser

data class SampleChooserState(
    val guitarSamples: List<SampleUi>,
    val drumSamples: List<SampleUi>,
    val trumpetSamples: List<SampleUi>
)

data class SampleUi(
    val id: String,
    val title: String,
    val isSelected: Boolean
)