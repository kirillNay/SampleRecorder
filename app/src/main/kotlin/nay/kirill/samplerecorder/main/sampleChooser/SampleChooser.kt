package nay.kirill.samplerecorder.main.sampleChooser

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
import nay.kirill.samplerecorder.main.MainIntent
import nay.kirill.samplerecorder.theme.SampleRecorderTheme

@Composable
internal fun SampleChooser(
    state: SampleChooserUIState,
    accept: (MainIntent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        state.sampleGroups.forEach { group ->
            Sample(
                groupState = group
            ) {
                accept(MainIntent.SelectSample.Default(type = group.type))
            }
        }
    }
}

@Composable
private fun Sample(
    modifier: Modifier = Modifier,
    groupState: SampleGroupUi,
    onClick: () -> Unit
) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(if (groupState.isSelected) 84.dp else 76.dp)
                .clip(CircleShape)
                .run {
                    if (groupState.isSelected) {
                        border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    } else {
                        this
                    }
                }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = painterResource(id = groupState.iconId),
                contentScale = ContentScale.Crop,
                contentDescription = groupState.contentDescription,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier.padding(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val title = stringResource(id = groupState.titleId)
        val text = remember(groupState.isSelected) {
            groupState.samples.find { it.isSelected }?.name ?: title
        }
        Text(
            text = text,
            style = if (groupState.isSelected) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            color = if (groupState.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun SampleChooserPreview() {
    SampleRecorderTheme {
        SampleChooser(
            state = SampleChooserUIState(
                listOf()
            ),
            accept = {}
        )
    }
}