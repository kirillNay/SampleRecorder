package nay.kirill.samplerecorder.domain.model

data class Sample(
    val name: String,
    val assetName: String,
    val type: SampleType
) {
    val id = counter++
    companion object {
        var counter = 0
    }
}