package nay.kirill.samplerecorder.presentation.main.audioController

import androidx.compose.runtime.Immutable

@Immutable
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