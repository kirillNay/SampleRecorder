package nay.kirill.samplerecorder.player

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import nay.kirill.samplerecorder.domain.Player

internal class PlayerImpl(
    private val context: Context
) : Player {

    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer: MediaPlayer get() = checkNotNull(_mediaPlayer)

    override val state = MutableStateFlow<Player.State>(Player.State.UNKNOWN)

    override val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    override fun create(resourceId: Int) {
        _mediaPlayer?.stop()
        _mediaPlayer?.release()
        _mediaPlayer = MediaPlayer.create(context, resourceId)
        _mediaPlayer?.setOnCompletionListener {
            state.value = Player.State.Completed
        }
        _mediaPlayer?.setOnErrorListener { _, what, _ ->
            state.value = Player.State.Error(what)
            true
        }
        _mediaPlayer?.setOnPreparedListener {
            state.value = Player.State.Prepared
        }
    }

    override fun playLoop() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        state.value = Player.State.Play
    }

    override fun playOnce() {
        mediaPlayer.isLooping = false
        mediaPlayer.start()
        state.value = Player.State.Play
    }

    override fun pause() {
        mediaPlayer.pause()
        state.value = Player.State.Pause
    }

}