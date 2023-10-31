package nay.kirill.samplerecorder.main

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooserState
import nay.kirill.samplerecorder.main.sampleChooser.SampleGroup
import nay.kirill.samplerecorder.main.sampleChooser.SampleType

data class MainState(
    val chooserState: SampleChooserState
) {

    companion object {

        val initial = MainState(
            chooserState = SampleChooserState(
                listOf(
                    SampleGroup(
                        type = SampleType.GUITAR,
                        titleId = R.string.guitar_sample,
                        iconId = R.drawable.ic_guitar_sample,
                        contentDescription = "Select guitar sample",
                        samples = listOf()
                    ),
                    SampleGroup(
                        type = SampleType.DRUM,
                        titleId = R.string.drum_sample,
                        iconId = R.drawable.ic_drums_sample,
                        contentDescription = "Select drums sample",
                        samples = listOf()
                    ),
                    SampleGroup(
                        type = SampleType.TRUMPET,
                        titleId = R.string.trumpet_sample,
                        iconId = R.drawable.ic_trumpet_sample,
                        contentDescription = "Select trumpet sample",
                        samples = listOf()
                    )
                ),
            )
        )

    }
}