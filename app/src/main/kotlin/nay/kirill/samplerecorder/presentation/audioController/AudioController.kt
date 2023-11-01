package nay.kirill.samplerecorder.presentation.audioController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.theme.SampleRecorderTheme

@Composable
fun AudioController(
    modifier: Modifier = Modifier,
    state: AudioControllerState
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

    Row (
        modifier = modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = stringResource(id = R.string.volume),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .vertical()
                .rotate(-90F)
                .padding(bottom = 6.dp),
        )
        Column (
            modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val primaryColor = MaterialTheme.colorScheme.primary

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
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
                            Size(5F,size.height),
                            CornerRadius(10F)
                        )
                    }
            )
            Text(
                text = stringResource(id = R.string.speed),
                style = MaterialTheme.typography.bodyMedium,
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
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)) {
            AudioController(
                state = AudioControllerState(1F,1F)
            )
        }
    }
}