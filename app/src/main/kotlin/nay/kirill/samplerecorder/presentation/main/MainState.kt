package nay.kirill.samplerecorder.presentation.main

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.model.SampleType
import nay.kirill.samplerecorder.presentation.main.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.main.layers.LayersBottomSheetState
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
    val layers: List<Layer> = emptyList(),
    val expandedType: SampleType? = null,
    val isPlaying: Boolean = false,
    val amplitude: List<Float>? = null,
    val progress: Float = 0F,
    val initialSpeedScale: Float = (INITIAL_SPEED_VALUE - MIN_SPEED_VALUE) / (MAX_SPEED_VALUE - MIN_SPEED_VALUE),
    val initialVolumeScale: Float = (INITIAL_VOLUME_VALUE - MIN_VOLUME_VALUE) / (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE),
    val duration: Int = 0,
    val isLayersOpen: Boolean = false,
    val isRecording: Boolean = false
) {

    val selectedSample: Sample? get() = currentLayer.sample

}

sealed interface MainUIState {

    val playerControllerState: PlayerControllerState

    val layersBottomSheetState: LayersBottomSheetState

    data class Recording(
        override val playerControllerState: PlayerControllerState.EmptySample,
        override val layersBottomSheetState: LayersBottomSheetState
    ) : MainUIState

    data class Empty(
        override val playerControllerState: PlayerControllerState.EmptySample,
        override val layersBottomSheetState: LayersBottomSheetState,
        val chooserState: SampleChooserUIState,
    ) : MainUIState

    data class Sampling(
        override val playerControllerState: PlayerControllerState,
        override val layersBottomSheetState: LayersBottomSheetState,
        val chooserState: SampleChooserUIState,
        val timeline: PlayerTimelineState,
        val audioControllerState: AudioControllerState
    ) : MainUIState

}

data class LayerUi(
    val id: Int,
    val name: String,
    val isSelected: Boolean
)

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
            playerControllerState = PlayerControllerState.EmptySample(
                layerName = "Слой 1",
                isRecording = false
            ),
            layersBottomSheetState = LayersBottomSheetState(
                opened = false,
                layers = emptyList(),
                editAvailable = false
            )
        ),
        MainUIState.Empty(
            chooserState = SampleChooserUIState(sampleGroups = sampleGroups1),
            playerControllerState = PlayerControllerState.EmptySample(
                layerName = "Слой 1",
                isRecording = true
            ),
            layersBottomSheetState = LayersBottomSheetState(
                opened = false,
                layers = emptyList(),
                editAvailable = false
            )
        ),
    )

}