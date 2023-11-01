package nay.kirill.samplerecorder.main

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.main.playerController.AmplitudeNode
import nay.kirill.samplerecorder.main.playerController.PlayerControllerState
import nay.kirill.samplerecorder.main.playerController.PlayerTimelineState
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.main.sampleChooser.SampleGroupUi
import nay.kirill.samplerecorder.main.sampleChooser.SampleUi

class MainStateConverter : (MainState) -> MainUIState {

    override fun invoke(state: MainState): MainUIState {
        return MainUIState(
            chooserState = SampleChooserUIState(
                sampleGroups = state.samples.convertToGroups(state.selectedSampleId, state.expandedType)
            ),
            playerControllerState = PlayerControllerState(
                playingIcon = if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                contentDescription = if (state.isPlaying) "Stop" else "Play",
                isEnabled = state.selectedSampleId != null,
            ),
            timeline = state.amplitude?.convertToTimeline(state.progress) ?: PlayerTimelineState.Empty
        )
    }

    private fun List<Float>.convertToTimeline(progress: Float): PlayerTimelineState = PlayerTimelineState.Data(
        amplitude = mapIndexed { index, value ->
            AmplitudeNode(
                value = value,
                isPlayed = (index / size.toFloat()) <= progress
            )
        }
    )

    private fun List<Sample>.convertToGroups(selectedSampleId: Int?, expandedType: SampleType?): List<SampleGroupUi> {
        return groupBy { it.type }.map { (type, samples) ->
            when (type) {
                SampleType.GUITAR -> SampleGroupUi(
                    type = SampleType.GUITAR,
                    titleId = R.string.guitar_sample,
                    iconId = R.drawable.ic_guitar_sample,
                    contentDescription = "Select guitar sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId },
                    isExpanded = expandedType == type,
                    isShort = expandedType != null && expandedType != type
                )
                SampleType.DRUM -> SampleGroupUi(
                    type = SampleType.DRUM,
                    titleId = R.string.drum_sample,
                    iconId = R.drawable.ic_drums_sample,
                    contentDescription = "Select drum sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId },
                    isExpanded = expandedType == type,
                    isShort = expandedType != null && expandedType != type
                )
                SampleType.TRUMPET -> SampleGroupUi(
                    type = SampleType.TRUMPET,
                    titleId = R.string.trumpet_sample,
                    iconId = R.drawable.ic_trumpet_sample,
                    contentDescription = "Select guitar sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId },
                    isExpanded = expandedType == type,
                    isShort = expandedType != null && expandedType != type
                )
            }
        }
    }

    private fun Sample.toUi(selectedSampleId: Int?): SampleUi = SampleUi(
        id = id,
        name = name,
        isSelected = id == selectedSampleId
    )

}