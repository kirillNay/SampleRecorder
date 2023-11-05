package nay.kirill.samplerecorder.domain

import kotlinx.coroutines.flow.Flow
import nay.kirill.samplerecorder.domain.model.Sample

interface Player {

    fun create(samples: List<Sample>)

    fun playLoop(sampleId: Int)

    fun pause(sampleId: Int)

    fun resume(sampleId: Int)

    fun stop(sampleId: Int)

    fun setSpeed(sampleId: Int, speed: Float)

    fun setVolume(sampleId: Int, volume: Float)

    fun seekTo(sampleId: Int, value: Float)

    suspend fun getAmplitude(sampleId: Int): Result<List<Float>>

    fun isPlaying(sampleId: Int): Boolean

    fun observeProgress(sampleId: Int): Flow<Float>

    fun getDuration(sampleId: Int) : Int

}