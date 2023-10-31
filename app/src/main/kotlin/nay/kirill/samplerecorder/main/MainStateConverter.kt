package nay.kirill.samplerecorder.main

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.main.sampleChooser.SampleGroupUi
import nay.kirill.samplerecorder.main.sampleChooser.SampleUi

class MainStateConverter : (MainState) -> MainUIState {

    override fun invoke(state: MainState): MainUIState {
        return MainUIState(
            chooserState = SampleChooserUIState(
                sampleGroups = state.samples.convertToGroups(state.selectedSampleId)
            )
        )
    }

    private fun List<Sample>.convertToGroups(selectedSampleId: Int?): List<SampleGroupUi> {
        return groupBy { it.type }.map { (type, samples) ->
            when (type) {
                SampleType.GUITAR -> SampleGroupUi(
                    type = SampleType.GUITAR,
                    titleId = R.string.guitar_sample,
                    iconId = R.drawable.ic_guitar_sample,
                    contentDescription = "Select guitar sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId }
                )
                SampleType.DRUM -> SampleGroupUi(
                    type = SampleType.DRUM,
                    titleId = R.string.drum_sample,
                    iconId = R.drawable.ic_drums_sample,
                    contentDescription = "Select drum sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId }
                )
                SampleType.TRUMPET -> SampleGroupUi(
                    type = SampleType.TRUMPET,
                    titleId = R.string.trumpet_sample,
                    iconId = R.drawable.ic_trumpet_sample,
                    contentDescription = "Select guitar sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId }
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