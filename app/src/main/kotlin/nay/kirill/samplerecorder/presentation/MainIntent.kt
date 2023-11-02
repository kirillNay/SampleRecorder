package nay.kirill.samplerecorder.presentation

import nay.kirill.samplerecorder.domain.SampleType

sealed interface MainIntent {

    sealed interface SelectSample : MainIntent {

        data class Expand(val type: SampleType) : SelectSample

        data class Default(val type: SampleType) : SelectSample

        data class Sample(val id: Int) : SelectSample

    }

    sealed interface PlayerController : MainIntent {

        object OnPlayButton : PlayerController

        data class Seek(val position: Float) : PlayerController

        data class LayersModal(val open: Boolean) : PlayerController

    }

    sealed interface AudioParams : MainIntent {

        data class NewParams(
            val volume: Float,
            val speed: Float
        ) : AudioParams

    }
}