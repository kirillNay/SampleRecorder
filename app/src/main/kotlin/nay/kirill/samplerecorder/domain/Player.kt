package nay.kirill.samplerecorder.domain

import kotlinx.coroutines.flow.Flow

interface Player {

    fun create(resourceId: Int, speed: Float = 1F, volume: Float = 1F)

    fun playLoop()

    fun playOnce()

    fun pause()

    fun stopAndRelease()

    fun setSpeed(speed: Float)

    fun setVolume(volume: Float)

    fun seekTo(value: Float)

    suspend fun getAmplitude(): Result<List<Float>>

    val isPlaying: Boolean

    val state: Flow<State>

    val progress: Flow<Float>

    val duration: Int

    sealed interface State {

        object UNKNOWN : State

        object Completed : State

        data class Error(val errorCode: Int) : State

        object Prepared : State

        object Play : State

        object Pause : State

        object Released : State

    }

}