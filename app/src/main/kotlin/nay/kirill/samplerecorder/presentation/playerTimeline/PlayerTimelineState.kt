package nay.kirill.samplerecorder.presentation.playerTimeline

sealed interface PlayerTimelineState {

    data class Data(
        val amplitude: List<AmplitudeNode>
    ) : PlayerTimelineState

    object Empty : PlayerTimelineState

}

data class AmplitudeNode(
    val value: Float,
    val isPlayed: Boolean
)