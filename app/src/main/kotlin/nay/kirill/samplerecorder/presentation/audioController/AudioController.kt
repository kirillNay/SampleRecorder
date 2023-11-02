package nay.kirill.samplerecorder.presentation.audioController

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.MainIntent
import nay.kirill.samplerecorder.theme.SampleRecorderTheme

@Composable
fun AudioController(
    modifier: Modifier = Modifier,
    state: AudioControllerState,
    accept: (MainIntent) -> Unit
) {
    fun Modifier.vertical() = layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.volume),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .vertical()
                .rotate(-90F)
                .padding(bottom = 6.dp),
        )
        Column(
            modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val primaryColor = MaterialTheme.colorScheme.primary

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background
                            ),
                            start = Offset(0F,Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0F)
                        )
                    )
                    .drawBehind {
                        drawRoundRect(
                            primaryColor,
                            Offset(0f, size.height - 5F / 2),
                            Size(size.width, 5F),
                            CornerRadius(10F)
                        )

                        drawRoundRect(
                            primaryColor,
                            Offset(0F, 0F),
                            Size(5F, size.height),
                            CornerRadius(10F)
                        )
                    }
            ) {
                val parentWidthPx = constraints.maxWidth
                val parentHeightPx = constraints.maxHeight

                val circleDiameter = 12.dp
                val circleDiameterPx = LocalDensity.current.run { circleDiameter.toPx() }

                val initialX = constraints.maxWidth * state.initialSpeed - circleDiameterPx / 2
                val initialY = constraints.maxHeight * state.initialVolume - circleDiameterPx / 2

                var offset by remember {
                    mutableStateOf(Offset(initialX, initialY))
                }

                Box(
                    modifier = Modifier
                        .offset { offset.round() }
                        .size(circleDiameter)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { _, dragAmount ->
                                    val summed = offset + dragAmount
                                    val x = summed.x.coerceIn(0f, parentWidthPx - circleDiameterPx)
                                    val y = summed.y.coerceIn(0f, parentHeightPx - circleDiameterPx)
                                    offset = Offset(x, y)

                                    accept(MainIntent.AudioParams.NewParams(y / parentHeightPx, x / parentWidthPx))
                                }
                            )
                        }
                )
            }
            Text(
                text = stringResource(id = R.string.speed),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 6.dp),
            )
        }
    }
}

@Preview
@Composable
private fun AudioControllerPreview() {
    SampleRecorderTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            AudioController(
                state = AudioControllerState(1F, 1F)
            ) {}
        }
    }
}