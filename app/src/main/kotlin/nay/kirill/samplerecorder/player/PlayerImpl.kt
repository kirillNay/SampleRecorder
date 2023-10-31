package nay.kirill.samplerecorder.player

import android.content.Context
import android.media.MediaPlayer
import nay.kirill.samplerecorder.domain.Player

internal class PlayerImpl(
    private val context: Context
) : Player {

    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer: MediaPlayer get() = checkNotNull(_mediaPlayer)

    override fun create(resourceId: Int) {
        _mediaPlayer?.stop()
        _mediaPlayer?.release()
        _mediaPlayer = MediaPlayer.create(context, resourceId)
    }

    override fun playLoop() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    override fun playOnce() {
        mediaPlayer.isLooping = false
        mediaPlayer.start()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

}