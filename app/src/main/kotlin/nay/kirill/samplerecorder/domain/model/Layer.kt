package nay.kirill.samplerecorder.domain.model

data class Layer(
    val id: Int,
    val sample: Sample?,
    val speed: Float,
    val volume: Float
)