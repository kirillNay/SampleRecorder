package nay.kirill.samplerecorder.presentation.main

import nay.kirill.samplerecorder.domain.model.SampleType

sealed interface MainIntent {

    sealed interface Lifecycle : MainIntent {

        object OnPause : Lifecycle

        object OnResume : Lifecycle

        object OnDestroy : Lifecycle

    }

    sealed interface SelectSample : MainIntent {

        data class Expand(val type: SampleType) : SelectSample

        data class Default(val type: SampleType) : SelectSample

        data class Sample(val id: Int) : SelectSample

    }

    sealed interface PlayerController : MainIntent {

        object OnPlayButton : PlayerController

        data class Seek(val position: Float) : PlayerController

        data class LayersModal(val open: Boolean) : PlayerController

        object OnRecord : PlayerController

        object OnFinalRecord : PlayerController

    }

    sealed interface Layers : MainIntent {

        data class SelectLayer(val id: Int) : Layers

        object CreateNew : Layers

        data class RemoveLayer(val id: Int) : Layers

        data class SetPlaying(val id: Int, val isPlaying: Boolean) : Layers

    }

    sealed interface AudioParams : MainIntent {

        data class NewParams(
            val volume: Float,
            val speed: Float
        ) : AudioParams

    }

    sealed interface FinalRecord : MainIntent {

        object Share: FinalRecord

        object Reset : FinalRecord

        object Open : FinalRecord

        object Visualising : FinalRecord

    }

    sealed interface Visualising : MainIntent {

        object Play : Visualising

        object Back : Visualising

        object ToStart : Visualising

        object ToEnd : Visualising

        data class NameEdit(val name: String) : Visualising

    }
}