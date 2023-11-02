package nay.kirill.samplerecorder.player

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import linc.com.amplituda.Amplituda
import nay.kirill.samplerecorder.domain.Player
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class PlayerImpl(
    private val context: Context,
    private val amplituda: Amplituda
) : Player {

    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer: MediaPlayer get() = checkNotNull(_mediaPlayer)

    private var mediaResId: Int? = null

    override val state = MutableStateFlow<Player.State>(Player.State.UNKNOWN)

    private val _progress = MutableStateFlow(0F)
    override val progress: Flow<Float> = _progress.asStateFlow()
    private var progressTimer: Timer? = null

    override val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    override fun create(resourceId: Int) {
        mediaResId = resourceId

        stopProgressTimer()
        _mediaPlayer?.stop()
        _mediaPlayer?.release()
        _mediaPlayer = MediaPlayer.create(context, resourceId)
        _mediaPlayer?.setOnCompletionListener {
            state.value = Player.State.Completed
            if (!mediaPlayer.isLooping) stopProgressTimer()
            _progress.value = 1F
        }
        _mediaPlayer?.setOnErrorListener { _, what, _ ->
            state.value = Player.State.Error(what)
            stopProgressTimer()
            true
        }
        _mediaPlayer?.setOnPreparedListener {
            state.value = Player.State.Prepared
        }
    }

    private fun startProgressTimer() {
        progressTimer = timer(
            period = mediaPlayer.duration / AMPLITUDE_COUNT.toLong()
        ) {
            _progress.value = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
        }
    }

    private fun stopProgressTimer() {
        progressTimer?.cancel()
        progressTimer = null
    }

    override suspend fun getAmplitude() = suspendCoroutine<Result<List<Float>>> { continuation ->
        checkNotNull(mediaResId)

        amplituda.processAudio(mediaResId!!).get(
            // success listener
            { result ->
                continuation.resume(
                    Result.success(
                        AmplitudeConverter.map(
                            AMPLITUDE_COUNT,
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

    override fun playLoop() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        startProgressTimer()

        state.value = Player.State.Play
    }

    override fun playOnce() {
        mediaPlayer.isLooping = false
        mediaPlayer.start()
        startProgressTimer()

        state.value = Player.State.Play
    }

    override fun pause() {
        stopProgressTimer()
        mediaPlayer.pause()
        state.value = Player.State.Pause
    }

    override fun setSpeed(speed: Float) {
        if (!mediaPlayer.isPlaying) return
        mediaPlayer.playbackParams = mediaPlayer.playbackParams.apply { this.speed = speed }
    }

    override fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }

    override fun seekTo(value: Float) {
        _progress.value = value
        mediaPlayer.seekTo((mediaPlayer.duration * value).toInt())
    }

    companion object {

        const val AMPLITUDE_COUNT = 70

    }

}