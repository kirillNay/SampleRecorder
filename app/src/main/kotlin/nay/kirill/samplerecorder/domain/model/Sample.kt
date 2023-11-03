package nay.kirill.samplerecorder.domain.model

data class Sample(
    val name: String,
    val resourceId: Int,
    val type: SampleType
) {
    val id = resourceId
}