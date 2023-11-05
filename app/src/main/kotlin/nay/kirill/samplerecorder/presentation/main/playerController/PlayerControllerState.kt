package nay.kirill.samplerecorder.presentation.main.playerController

import androidx.annotation.DrawableRes
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import nay.kirill.samplerecorder.R

sealed interface PlayerControllerState {

    val layerName: String

    val isRecording: Boolean

    val isFinalRecording: Boolean

    val isRecordAvailable: Boolean

    data class Sampling(
        override val layerName: String,
        @DrawableRes val playingIcon: Int,
        val contentDescription: String,
        override val isRecording: Boolean,
        override val isFinalRecording: Boolean,
        override val isRecordAvailable: Boolean
    ) : PlayerControllerState

    data class EmptySample(
        override val layerName: String,
        override val isRecording: Boolean,
        override val isFinalRecording: Boolean,
        override val isRecordAvailable: Boolean
    ) : PlayerControllerState

}

internal class PlayerControllerStateProvider : PreviewParameterProvider<PlayerControllerState> {

    override val values: Sequence<PlayerControllerState> = sequenceOf()

}