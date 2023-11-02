package nay.kirill.samplerecorder.domain

import kotlinx.coroutines.flow.Flow

interface Player {

    fun create(resourceId: Int)

    fun playLoop()

    fun playOnce()

    fun pause()

    fun setSpeed(speed: Float)

    fun setVolume(volume: Float)

    suspend fun getAmplitude(): Result<List<Float>>

    val isPlaying: Boolean

    val state: Flow<State>

    val progress: Flow<Float>

    sealed interface State {

        object UNKNOWN : State

        object Completed : State

        data class Error(val errorCode: Int) : State

        object Prepared : State

        object Play : State

        object Pause : State

    }

}