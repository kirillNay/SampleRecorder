package nay.kirill.samplerecorder.presentation.main

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.model.SampleType
import nay.kirill.samplerecorder.presentation.main.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.main.layers.LayersBottomSheetState
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.main.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleGroupUi

sealed interface MainUIState

sealed interface FinalRecordUIState : MainUIState {

    data class Recording(
        val playerControllerState: PlayerControllerState.EmptySample,
        val layersBottomSheetState: LayersBottomSheetState
    ) : FinalRecordUIState

    object Saving : FinalRecordUIState

    object Complete : FinalRecordUIState

}

@Immutable
sealed interface SamplingUIState : MainUIState {

    val playerControllerState: PlayerControllerState

    val layersBottomSheetState: LayersBottomSheetState

    data class Recording(
        override val playerControllerState: PlayerControllerState.EmptySample,
        override val layersBottomSheetState: LayersBottomSheetState
    ) : SamplingUIState

    data class Empty(
        override val playerControllerState: PlayerControllerState.EmptySample,
        override val layersBottomSheetState: LayersBottomSheetState,
        val chooserState: SampleChooserUIState,
    ) : SamplingUIState

    data class Sampling(
        override val playerControllerState: PlayerControllerState,
        override val layersBottomSheetState: LayersBottomSheetState,
        val chooserState: SampleChooserUIState,
        val timeline: PlayerTimelineState,
        val audioControllerState: AudioControllerState,
        val isVocal: Boolean
    ) : SamplingUIState

}

object FailureUIState : MainUIState

data class LayerUi(
    val id: Int,
    val name: String,
    val isSelected: Boolean,
    val isPlaying: Boolean?
)

// Previews
internal class MainUIStateProvider : PreviewParameterProvider<SamplingUIState> {

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

    override val values: Sequence<SamplingUIState> = sequenceOf()

}