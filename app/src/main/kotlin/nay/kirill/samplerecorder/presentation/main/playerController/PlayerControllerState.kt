package nay.kirill.samplerecorder.presentation.main.playerController

import androidx.annotation.DrawableRes

data class PlayerControllerState(
    @DrawableRes val playingIcon: Int,
    val contentDescription: String,
    val layerName: String,
)