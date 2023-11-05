@file:OptIn(ExperimentalTextApi::class)

package nay.kirill.samplerecorder.presentation.main.audioController

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.MainIntent
import nay.kirill.samplerecorder.presentation.main.theme.SampleRecorderTheme

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
            ) {
                val parentWidthPx = constraints.maxWidth
                val parentHeightPx = constraints.maxHeight

                val circleDiameter = 8.dp
                val circleDiameterPx = LocalDensity.current.run { circleDiameter.toPx() }

                val initialX = constraints.maxWidth * state.speed - circleDiameterPx / 2
                val initialY = constraints.maxHeight * state.volume - circleDiameterPx / 2

                var offset by remember(state.layerId) {
                    mutableStateOf(Offset(initialX, initialY))
                }

                val textMeasure = rememberTextMeasurer()
                val textStyle = MaterialTheme.typography.bodySmall
                val color = MaterialTheme.colorScheme.outlineVariant

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.background
                                ),
                                start = Offset(0F, Float.POSITIVE_INFINITY),
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

                            //drawing steps
                            drawRoundRect(
                                primaryColor,
                                Offset(size.width * state.stepSpeedScale, size.height - 50F),
                                Size(3F, 50F)
                            )

                            drawText(
                                textMeasurer = textMeasure,
                                text = state.initialSpeedText,
                                style = textStyle.copy(color = color),
                                topLeft = Offset(size.width * state.stepSpeedScale - 15F, size.height - 120F)
                            )

                            drawRoundRect(
                                primaryColor,
                                Offset(0F, size.height * state.stepVolumeScale),
                                Size(50F, 3F)
                            )

                            drawText(
                                textMeasurer = textMeasure,
                                text = state.initialVolumeText,
                                style = textStyle.copy(color = color),
                                topLeft = Offset(80F, size.height * state.stepVolumeScale - 30F)
                            )

                            drawRoundRect(
                                primaryColor,
                                Offset(size.width - 3F, size.height - 50F),
                                Size(3F, 50F)
                            )

                            drawText(
                                textMeasurer = textMeasure,
                                text = state.maxSpeedText,
                                style = textStyle.copy(color = color),
                                topLeft = Offset(size.width - 50F, size.height - 120F)
                            )

                            drawRoundRect(
                                primaryColor,
                                Offset(0F, 0F),
                                Size(50F, 3F)
                            )

                            drawText(
                                textMeasurer = textMeasure,
                                text = state.maxVolumeText,
                                style = textStyle.copy(color = color),
                                topLeft = Offset(80F, 0F)
                            )
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { detectedOffset ->
                                    offset = detectedOffset
                                    accept(MainIntent.AudioParams.NewParams(offset.y / parentHeightPx, offset.x / parentWidthPx))
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()

                                    val summed = offset + dragAmount
                                    val x = summed.x.coerceIn(0f, parentWidthPx - circleDiameterPx)
                                    val y = summed.y.coerceIn(0f, parentHeightPx - circleDiameterPx)
                                    offset = Offset(x, y)

                                    accept(MainIntent.AudioParams.NewParams(y / parentHeightPx, x / parentWidthPx))
                                }
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .offset { offset.round() }
                            .size(circleDiameter)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
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
                state = AudioControllerState(1, 0.5F, 0.5F, 0.5F, 0.5F, "1x", "1x", "2x", "2x")
            ) {}
        }
    }
}