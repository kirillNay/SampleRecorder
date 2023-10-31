package nay.kirill.samplerecorder.domain

data class Sample(
    val name: String,
    val resourceId: Int,
    val type: SampleType
) {
    val id = resourceId
}