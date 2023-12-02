package nay.kirill.samplerecorder.presentation.main

import androidx.annotation.DrawableRes
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.isActive
import nay.kirill.samplerecorder.R
import java.lang.Float.max
import java.lang.Float.min
import kotlin.random.Random

class ArtAnimationManager {

    var speed: Float = 1F

    var isPlaying: Boolean = false

    data class Angle(
        val x: Float = Random.nextFloat() * 2 - 1,
        val y: Float = Random.nextFloat() * 2 - 1
    )

    data class ArtState(
        @DrawableRes val id: Int,
        var offsetXScale: Float = Random.nextFloat(),
        var offsetYScale: Float = Random.nextFloat(),
        var angle: Angle = Angle()
    )

    val artsScaleState: List<ArtOffsetState>
        get() = arts.map { art ->
            ArtOffsetState(
                id = art.id,
                offsetXScale = art.offsetXScale,
                offsetYScale = art.offsetYScale
            )
        }

    private val arts = listOf(
        ArtState(R.drawable.art_1),
        ArtState(R.drawable.art_2),
        ArtState(R.drawable.art_3),
        ArtState(R.drawable.art_4),
        ArtState(R.drawable.art_5),
        ArtState(R.drawable.art_6),
        ArtState(R.drawable.art_7),
        ArtState(R.drawable.art_8),
        ArtState(R.drawable.art_9),
        ArtState(R.drawable.art_10)
    )

    fun observeArtsState(): Flow<List<ArtOffsetState>> = flow {
        while (currentCoroutineContext().isActive) {
            delay(DELAY)
            if (isPlaying) {
                arts.forEach { art ->
                    art.offsetXScale = min(1F, max(0F, art.offsetXScale + (speed * OFFSET * art.angle.x)))
                    art.offsetYScale = min(1F, max(0F, art.offsetYScale + (speed * OFFSET * art.angle.y)))

                    if (art.offsetYScale == 0F || art.offsetYScale == 1F) {
                        art.angle = art.angle.copy(
                            y = -art.angle.y
                        )
                    }

                    if (art.offsetXScale == 0F || art.offsetXScale == 1F) {
                        art.angle = art.angle.copy(
                            x = -art.angle.x,
                        )
                    }
                }
                emit(artsScaleState)
            }
        }
    }

    companion object {

        private const val DELAY = 50L

        private const val OFFSET = 0.02F

    }

}