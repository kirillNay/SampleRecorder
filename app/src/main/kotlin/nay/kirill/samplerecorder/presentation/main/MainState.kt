package nay.kirill.samplerecorder.presentation.main

import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.model.SampleType

const val MIN_SPEED_VALUE = 0.5F
const val MAX_SPEED_VALUE = 2F

const val MAX_VOLUME_VALUE = 2F
const val MIN_VOLUME_VALUE = 0F

const val INITIAL_SPEED_VALUE = 1F
const val INITIAL_VOLUME_VALUE = 1F

internal fun calculateSpeedScale(speed: Float) = (speed - MIN_SPEED_VALUE) / (MAX_SPEED_VALUE - MIN_SPEED_VALUE)

internal fun calculateVolumeScale(volume: Float) = (volume - MIN_VOLUME_VALUE) / (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE)

data class VisualisingState(
    val audioDuration: Int,
    val progress: Float,
    val isPlaying: Boolean,
    val name: String,
    val arts: List<ArtOffsetState>
)

data class MainState(
    val samples: List<Sample>,
    val currentLayerId: Int,
    val layers: List<Layer> = emptyList(),
    val expandedType: SampleType? = null,
    val amplitude: List<Float>? = null,
    val progress: Float = 0F,
    val initialSpeedScale: Float = calculateSpeedScale(INITIAL_SPEED_VALUE),
    val initialVolumeScale: Float = calculateVolumeScale(INITIAL_VOLUME_VALUE),
    val duration: Int = 0,
    val isLayersOpen: Boolean = false,
    val isVoiceRecording: Boolean = false,
    val finalRecordState: FinalRecordState = FinalRecordState.None,
    val exception: Throwable? = null,
    val visualisingState: VisualisingState? = null
) {

    val currentLayer: Layer? get() = layers.find { it.id == currentLayerId }

    val selectedSample: Sample? get() = currentLayer?.sample

    val isPlaying: Boolean get() = currentLayer?.isPlaying ?: false

}

sealed interface FinalRecordState {

    object None : FinalRecordState

    object Process : FinalRecordState

    object Saving : FinalRecordState

    data class Complete(val data: Player.FinalRecord) : FinalRecordState

}
