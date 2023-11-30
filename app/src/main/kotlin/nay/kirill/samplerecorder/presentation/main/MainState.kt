package nay.kirill.samplerecorder.presentation.main

import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.model.SampleType

const val MIN_SPEED_VALUE = 0.5F
const val MAX_SPEED_VALUE = 2F

const val MAX_VOLUME_VALUE = 2F
const val MIN_VOLUME_VALUE = 0F

const val INITIAL_SPEED_VALUE = 1F
const val INITIAL_VOLUME_VALUE = 1F

data class MainState(
    val samples: List<Sample>,
    val currentLayerId: Int,
    val layers: List<Layer> = emptyList(),
    val expandedType: SampleType? = null,
    val amplitude: List<Float>? = null,
    val progress: Float = 0F,
    val initialSpeedScale: Float = (INITIAL_SPEED_VALUE - MIN_SPEED_VALUE) / (MAX_SPEED_VALUE - MIN_SPEED_VALUE),
    val initialVolumeScale: Float = (INITIAL_VOLUME_VALUE - MIN_VOLUME_VALUE) / (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE),
    val duration: Int = 0,
    val isLayersOpen: Boolean = false,
    val isVoiceRecording: Boolean = false,
    val finalRecordState: FinalRecordState = FinalRecordState.None,
    val fileDirectory: String? = null,
    val exception: Throwable? = null
) {

    val currentLayer: Layer? get() = layers.find { it.id == currentLayerId }

    val selectedSample: Sample? get() = currentLayer?.sample

    val isPlaying: Boolean get() = currentLayer?.isPlaying ?: false

}

enum class FinalRecordState {

    None, Process, Saving, Complete

}
