package nay.kirill.samplerecorder.presentation

import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.presentation.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooserUIState

const val MIN_SPEED_VALUE = 0.5F
const val MAX_SPEED_VALUE = 2F

const val MAX_VOLUME_VALUE = 100F
const val MIN_VOLUME_VALUE = 0F

const val INITIAL_SPEED_VALUE = 1F
const val INITIAL_VOLUME_VALUE = 50F

data class MainState(
    val samples: List<Sample>,
    val selectedSampleId: Int? = null,
    val expandedType: SampleType? = null,
    val isPlaying: Boolean = false,
    val amplitude: List<Float>? = null,
    val progress: Float,
    val initialSpeedScale: Float,
    val initialVolumeScale: Float,
    val speed: Float = 1F,
    val volume: Float = 1F,
    val duration: Int = 0
) {

    val selectedSample: Sample? get() = samples.find { it.id == selectedSampleId }

}

sealed interface MainUIState {

    val chooserState: SampleChooserUIState

    data class Empty(
        override val chooserState: SampleChooserUIState
    ) : MainUIState

    data class Sampling(
        override val chooserState: SampleChooserUIState,
        val playerControllerState: PlayerControllerState,
        val timeline: PlayerTimelineState,
        val audioControllerState: AudioControllerState
    ) : MainUIState

}