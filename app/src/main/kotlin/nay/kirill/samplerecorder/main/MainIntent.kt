package nay.kirill.samplerecorder.main

import nay.kirill.samplerecorder.main.sampleChooser.SampleType

sealed interface MainIntent {

    sealed interface SelectSample : MainIntent {

        data class Default(val type: SampleType) : SelectSample

        data class Sample(val id: String) : SelectSample

    }

}