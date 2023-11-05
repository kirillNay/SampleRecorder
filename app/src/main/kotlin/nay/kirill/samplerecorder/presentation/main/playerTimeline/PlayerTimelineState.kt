package nay.kirill.samplerecorder.presentation.main.playerTimeline

sealed interface PlayerTimelineState {

    data class Data(
        val amplitude: List<Float>,
        val progress: Float,
        val duration: String,
        val currentPosition: String,
    ) : PlayerTimelineState

    object Empty : PlayerTimelineState

}