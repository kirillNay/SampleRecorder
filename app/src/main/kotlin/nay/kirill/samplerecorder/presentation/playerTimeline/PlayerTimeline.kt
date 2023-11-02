package nay.kirill.samplerecorder.presentation.playerTimeline

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import nay.kirill.samplerecorder.presentation.MainIntent
import nay.kirill.samplerecorder.theme.SampleRecorderTheme

@Composable
internal fun PlayerTimeline(
    modifier: Modifier = Modifier,
    state: PlayerTimelineState,
    accept: (MainIntent) -> Unit
) {
    when (state) {
        is PlayerTimelineState.Data -> {
            BoxWithConstraints (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            ){
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { change, _ ->
                                change.consume()
                                accept(MainIntent.Player.Seek((change.position.x) / constraints.maxWidth.toFloat()))
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { offset ->
                                    accept(MainIntent.Player.Seek((offset.x) / constraints.maxWidth.toFloat()))
                                }
                            )
                        },
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    state.amplitude.forEachIndexed { index, chart ->
                        val padding by animateDpAsState(
                            targetValue = max(0.dp, 69.dp - chart * 70.dp)
                        )

                        val primaryColor = MaterialTheme.colorScheme.primary
                        val containerColor = MaterialTheme.colorScheme.primaryContainer
                        val color = remember(state.progress) {
                            when ((index / state.amplitude.size.toFloat()) <= state.progress) {
                                true -> primaryColor
                                false -> containerColor
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = padding / 2)
                                .width(2.dp)
                                .clip(RoundedCornerShape(50))
                                .background(color)
                        )
                    }
                }
                Text(
                    text = state.currentPosition,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                )
                Text(
                    text = state.duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        }

        else -> Unit
    }
}

@Preview
@Composable
private fun PlayerTimelinePreview(

) {
    SampleRecorderTheme {
        PlayerTimeline(
            state = PlayerTimelineState.Data(
                amplitude = listOf(
                    0.5576923F,
                    0.21153846F,
                    0.48076922F,
                    0.40384614F,
                    0.115384616F,
                    0.15384616F,
                    0.17307693F,
                    0.1923077F,
                    0.44230768F,
                    0.0F,
                    0.34615386F,
                    0.25F,
                    0.26923078F,
                    0.26923078F,
                    0.23076923F,
                    0.21153846F,
                    0.1923077F,
                    0.30769232F,
                    0.30769232F,
                    0.23076923F,
                    0.25F,
                    0.26923078F,
                    0.25F,
                    0.32692307F,
                    0.23076923F,
                    0.21153846F,
                    0.15384616F,
                    0.13461539F,
                    0.07692308F,
                    0.34615386F,
                    0.30769232F,
                    0.26923078F,
                    0.26923078F,
                    0.59615386F,
                    0.34615386F,
                    0.48076922F,
                    0.26923078F,
                    0.17307693F,
                    0.21153846F,
                    0.21153846F,
                    0.23076923F,
                    0.01923077F,
                    0.01923077F,
                    0.01923077F,
                    0.26923078F,
                    0.26923078F,
                    0.23076923F,
                    0.26923078F,
                    0.23076923F,
                    0.1923077F,
                    0.32692307F,
                    0.28846154F,
                    0.17307693F,
                    0.1923077F,
                    0.25F,
                    0.03846154F,
                    0.3846154F,
                    0.25F,
                    0.26923078F,
                    0.15384616F,
                    0.13461539F,
                    0.09615385F,
                    0.3653846F,
                    0.34615386F,
                    0.28846154F,
                    0.15384616F,
                    0.90384614F,
                    0.25F,
                    0.01923077F,
                    0.28846154F,
                    0.5576923F,
                    0.32692307F,
                    0.32692307F,
                    0.21153846F,
                    0.03846154F,
                    0.0F,
                    0.42307693F,
                    0.28846154F,
                    0.28846154F,
                    0.26923078F,
                    0.28846154F,
                    0.23076923F,
                    0.9807692F,
                    0.48076922F,
                    0.17307693F,
                    0.01923077F,
                    0.90384614F,
                    0.44230768F,
                    0.30769232F,
                    0.23076923F,
                    0.21153846F,
                    0.21153846F,
                    0.23076923F,
                    0.28846154F,
                    0.23076923F,
                    0.01923077F,
                    0.1923077F,
                    0.17307693F,
                    0.1923077F,
                    0.03846154F
                ),
                progress = 0.5F,
                duration = "0:30",
                currentPosition = "0:15"
            )
        ) {

        }
    }
}