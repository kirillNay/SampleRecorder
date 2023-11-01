package nay.kirill.samplerecorder.presentation

import nay.kirill.samplerecorder.domain.SampleType

sealed interface MainIntent {

    sealed interface SelectSample : MainIntent {

        data class Expand(val type: SampleType) : SelectSample

        data class Default(val type: SampleType) : SelectSample

        data class Sample(val id: Int) : SelectSample

    }

    sealed interface Player : MainIntent {

        object OnPlayButton : Player

    }

}