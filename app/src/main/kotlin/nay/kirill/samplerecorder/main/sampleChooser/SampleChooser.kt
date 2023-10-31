package nay.kirill.samplerecorder.main.sampleChooser

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.theme.SampleRecorderTheme

@Composable
internal fun SampleChooser(
    state: SampleChooserState
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Sample(
            id = R.drawable.ic_guitar_sample,
            description = stringResource(R.string.guitar_sample),
            contentDescription = "Choose guitar sample",
            samples = state.guitarSamples
        )
        Sample(
            id = R.drawable.ic_drums_sample,
            description = stringResource(R.string.drum_sample),
            contentDescription = "Choose drum sample",
            samples = state.drumSamples
        )
        Sample(
            id = R.drawable.ic_trumpet_sample,
            description = stringResource(R.string.trumpet_sample),
            contentDescription = "Choose trumpet sample",
            samples = state.guitarSamples
        )
    }
}

@Composable
private fun Sample(
    @DrawableRes id: Int,
    description: String,
    contentDescription: String,
    samples: List<SampleUi>
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val isSelected = remember(samples) {
            samples.any { it.isSelected }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .apply {
                    if (isSelected) {
                        border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    }
                }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable(
                    onClick = {

                    },
                )
        ) {
            Image(
                painter = painterResource(id = id),
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val text = remember(isSelected) {
            samples.find { it.isSelected }?.title ?: description
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun SampleChooserPreview() {
    SampleRecorderTheme {
        SampleChooser(
            state = SampleChooserState(
                listOf(),
                listOf(),
                listOf()
            )
        )
    }
}