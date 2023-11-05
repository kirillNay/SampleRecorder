package nay.kirill.samplerecorder.presentation.main.audioController

data class AudioControllerState(
    val layerId: Int,
    val volume: Float,
    val speed: Float,

    val stepVolumeScale: Float,
    val stepSpeedScale: Float,
    val initialVolumeText: String,
    val initialSpeedText: String,
    val maxVolumeText: String,
    val maxSpeedText: String
)