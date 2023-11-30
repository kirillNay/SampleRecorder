package nay.kirill.samplerecorder.presentation.main.playerTimeline

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PlayerTimelineState {

    data class Data(
        val amplitude: List<Float>,
        val progress: Float,
        val duration: String,
        val currentPosition: String,
    ) : PlayerTimelineState

    object Empty : PlayerTimelineState

}