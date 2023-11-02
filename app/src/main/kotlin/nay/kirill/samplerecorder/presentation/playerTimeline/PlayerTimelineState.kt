package nay.kirill.samplerecorder.presentation.playerTimeline

sealed interface PlayerTimelineState {

    data class Data(
        val amplitude: List<Float>,
        val progress: Float
    ) : PlayerTimelineState

    object Empty : PlayerTimelineState

}