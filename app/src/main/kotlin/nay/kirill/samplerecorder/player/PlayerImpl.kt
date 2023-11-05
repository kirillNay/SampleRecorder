package nay.kirill.samplerecorder.player

import android.content.res.AssetManager
import android.util.Log
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import linc.com.amplituda.Amplituda
import nay.kirill.samplerecorder.domain.Player
import nay.kirill.samplerecorder.domain.model.Sample
import java.io.IOException
import java.lang.NullPointerException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlayerImpl(
    private val assetManager: AssetManager,
    private val amplituda: Amplituda
) : Player {

    private var samples: Map<Int, Sample> = mapOf()

    override fun create(samples: List<Sample>) {
        playerInitNative()

        samples.forEach { sample ->
            loadWavAsset(sample) {
                loadWavNative(it, sample.id)
            }
        }
        this.samples = samples.associateBy { it.id }
        startStreamNative()
    }

    override fun playLoop(sampleId: Int) {
        setLooping(sampleId, true)
        playNative(sampleId)
    }

    override fun pause(sampleId: Int) {
        pauseNative(sampleId)
    }

    override fun resume(sampleId: Int) {
        resumeNative(sampleId)
    }

    override fun stop(sampleId: Int) {
        stopNative(sampleId)
    }

    override fun setSpeed(sampleId: Int, speed: Float) {
        setSpeedNative(sampleId, speed)
    }

    override fun setVolume(sampleId: Int, volume: Float) {
        setVolumeNative(sampleId, volume)
    }

    override fun seekTo(sampleId: Int, value: Float) {
        seekToNative(sampleId, value)
    }

    override suspend fun getAmplitude(sampleId: Int) = suspendCoroutine<Result<List<Float>>> { continuation ->
        val sample = samples[sampleId]
        if(sample == null) {
            continuation.resume(Result.failure(NullPointerException()))
            return@suspendCoroutine
        }

        loadWavAsset(sample) {
            amplituda.processAudio(it).get(
                // success listener
                { result ->
                    continuation.resume(
                        Result.success(
                            AmplitudeConverter.map(
                                70,
                                result.amplitudesAsList()
                            )
                        )
                    )
                },
                // error listener
                { exception ->
                    continuation.resume(Result.failure(exception))
                }
            )
        }
    }

    override fun isPlaying(sampleId: Int): Boolean = isPlayingNative(sampleId)

    override fun observeProgress(sampleId: Int): Flow<Float> = flow {
        while (currentCoroutineContext().isActive) {
            delay(100)
            emit(getProgressNative(sampleId))
        }
    }
        .distinctUntilChanged()

    override fun getDuration(sampleId: Int): Int = getDurationNative(sampleId)

    override fun startRecording() {
        startVoiceRecordingNative()
    }

    override fun stopRecording(sampleId: Int) {
        stopVoiceRecordingNative(sampleId)
    }

    private fun loadWavAsset(sample: Sample, onLoad: (bytes: ByteArray) -> Unit) {
        try {
            val assetFD = assetManager.openFd(sample.assetName)
            val dataStream = assetFD.createInputStream()
            val dataLen = assetFD.length.toInt()
            val dataBytes = ByteArray(dataLen)
            dataStream.read(dataBytes, 0, dataLen)
            onLoad(dataBytes)
            assetFD.close()
        } catch (ex: IOException) {
            Log.i("PlayerImpl", "IOException$ex")
        }
    }

    private external fun playerInitNative()

    private external fun playNative(id: Int)

    private external fun resumeNative(id: Int)

    private external fun pauseNative(id: Int)

    private external fun stopNative(id: Int)

    private external fun setLooping(id: Int, isLooping: Boolean)

    private external fun loadWavNative(wavBytes: ByteArray, id: Int)

    private external fun startStreamNative()

    private external fun isPlayingNative(id: Int): Boolean

    private external fun getDurationNative(id: Int): Int

    private external fun getProgressNative(id: Int): Float

    private external fun seekToNative(id: Int, position: Float)

    private external fun setSpeedNative(id: Int, scale: Float)

    private external fun setVolumeNative(id: Int, scale: Float)

    private external fun startVoiceRecordingNative()

    private external fun stopVoiceRecordingNative(id: Int)

    private external fun startRecordingNative()

    private external fun stopRecordingNative()

    companion object {

        init {
            System.loadLibrary("samplerecorder")
        }
    }

}