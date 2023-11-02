package nay.kirill.samplerecorder.presentation.audioController

data class AudioControllerState(
    val initialVolume: Float,
    val initialSpeed: Float,

    val initialVolumeText: String,
    val initialSpeedText: String,
    val maxVolumeText: String,
    val maxSpeedText: String
)