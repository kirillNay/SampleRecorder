package nay.kirill.samplerecorder.presentation.main

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.model.SampleType
import nay.kirill.samplerecorder.presentation.main.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.main.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleGroupUi

const val MIN_SPEED_VALUE = 0.5F
const val MAX_SPEED_VALUE = 2F

const val MAX_VOLUME_VALUE = 100F
const val MIN_VOLUME_VALUE = 0F

const val INITIAL_SPEED_VALUE = 1F
const val INITIAL_VOLUME_VALUE = 50F

data class MainState(
    val samples: List<Sample>,
    val currentLayer: Layer,
    val expandedType: SampleType? = null,
    val isPlaying: Boolean = false,
    val amplitude: List<Float>? = null,
    val progress: Float,
    val initialSpeedScale: Float,
    val initialVolumeScale: Float,
    val duration: Int = 0,
    val isLayersOpen: Boolean = false
) {

    val selectedSample: Sample? get() = currentLayer.sample

}

sealed interface MainUIState {

    val chooserState: SampleChooserUIState

    val isLayersModalOpen: Boolean

    data class Empty(
        override val chooserState: SampleChooserUIState,
        override val isLayersModalOpen: Boolean,
        val layerName: String
    ) : MainUIState

    data class Sampling(
        override val chooserState: SampleChooserUIState,
        override val isLayersModalOpen: Boolean,
        val playerControllerState: PlayerControllerState,
        val timeline: PlayerTimelineState,
        val audioControllerState: AudioControllerState
    ) : MainUIState

}

// Previews
internal class MainUIStateProvider : PreviewParameterProvider<MainUIState> {

    private val sampleGroups = listOf(
        SampleGroupUi(
            type = SampleType.GUITAR,
            titleId = R.string.guitar_sample,
            iconId = R.drawable.ic_guitar_sample,
            contentDescription = "Select guitar sample",
            samples = listOf(),
            isSelected = true,
            isExpanded = false,
            isShort = false
        ),
        SampleGroupUi(
            type = SampleType.DRUM,
            titleId = R.string.drum_sample,
            iconId = R.drawable.ic_drums_sample,
            contentDescription = "Select drums sample",
            samples = listOf(),
            isSelected = false,
            isExpanded = false,
            isShort = false
        ),
        SampleGroupUi(
            type = SampleType.HORN,
            titleId = R.string.trumpet_sample,
            iconId = R.drawable.ic_trumpet_sample,
            contentDescription = "Select trumpet sample",
            samples = listOf(),
            isSelected = false,
            isExpanded = false,
            isShort = false
        )
    )

    private val sampleGroups1 = listOf(
        SampleGroupUi(
            type = SampleType.GUITAR,
            titleId = R.string.guitar_sample,
            iconId = R.drawable.ic_guitar_sample,
            contentDescription = "Select guitar sample",
            samples = listOf(),
            isSelected = false,
            isExpanded = true,
            isShort = false
        ),
        SampleGroupUi(
            type = SampleType.DRUM,
            titleId = R.string.drum_sample,
            iconId = R.drawable.ic_drums_sample,
            contentDescription = "Select drums sample",
            samples = listOf(),
            isSelected = false,
            isExpanded = false,
            isShort = true
        ),
        SampleGroupUi(
            type = SampleType.HORN,
            titleId = R.string.trumpet_sample,
            iconId = R.drawable.ic_trumpet_sample,
            contentDescription = "Select trumpet sample",
            samples = listOf(),
            isSelected = false,
            isExpanded = false,
            isShort = true
        )
    )

    override val values: Sequence<MainUIState> = sequenceOf(
        MainUIState.Empty(
            chooserState = SampleChooserUIState(sampleGroups = sampleGroups),
            isLayersModalOpen = false,
            layerName = "Layer 1"
        ),
        MainUIState.Empty(
            chooserState = SampleChooserUIState(sampleGroups = sampleGroups1),
            isLayersModalOpen = true,
            layerName = "Layer 1"
        ),
    )

}