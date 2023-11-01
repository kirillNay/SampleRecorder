package nay.kirill.samplerecorder.presentation.playerController

import androidx.annotation.DrawableRes

data class PlayerControllerState(
    @DrawableRes val playingIcon: Int,
    val contentDescription: String,
    val isEnabled: Boolean
)