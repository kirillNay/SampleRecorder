package nay.kirill.samplerecorder.domain

data class Sample(
    val id: Int,
    val name: String,
    val resourceId: Int,
    val type: SampleType
)