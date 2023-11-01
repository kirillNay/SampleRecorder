package nay.kirill.samplerecorder.presentation.sampleChooser

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.presentation.MainIntent

@Composable
internal fun SampleChooser(
    modifier: Modifier = Modifier,
    state: SampleChooserUIState,
    accept: (MainIntent.SelectSample) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        state.sampleGroups.forEach { group ->
            Sample(
                groupState = group,
                accept = accept
            )
        }
    }
}

@Composable
private fun Sample(
    modifier: Modifier = Modifier,
    groupState: SampleGroupUi,
    accept: (MainIntent.SelectSample) -> Unit
) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SampleButton(
            groupState,
            accept
        )

        if (!groupState.isExpanded && !groupState.isShort) {
            val title = stringResource(id = groupState.titleId)
            val text = remember(groupState.isSelected) {
                groupState.samples.find { it.isSelected }?.name ?: title
            }
            Text(
                text = text,
                style = if (groupState.isSelected) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                color = if (groupState.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SampleButton(
    groupState: SampleGroupUi,
    accept: (MainIntent.SelectSample) -> Unit
) {
    val width by animateDpAsState(
        targetValue = when {
            groupState.isSelected && !groupState.isExpanded -> 84.dp
            groupState.isExpanded -> 156.dp
            groupState.isShort -> 48.dp
            else -> 64.dp
        }
    )
    val height by animateDpAsState(
        targetValue = when {
            groupState.isSelected && !groupState.isExpanded -> 84.dp
            groupState.isExpanded -> 300.dp
            groupState.isShort -> 48.dp
            else -> 64.dp
        }
    )
    val iconColor by animateColorAsState(
        targetValue = when {
            groupState.isSelected || groupState.isExpanded -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.secondary
        }
    )
    val iconSize by animateDpAsState(
        targetValue = when {
            groupState.isShort -> 32.dp
            else -> 48.dp
        }
    )
    val elevation by animateDpAsState(
        targetValue = when {
            groupState.isSelected || groupState.isExpanded -> 26.dp
            else -> 6.dp
        }
    )

    ElevatedCard(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        modifier = Modifier
            .padding(18.dp)
            .width(width)
            .height(height)
            .combinedClickable(
                onClick = { accept(MainIntent.SelectSample.Default(type = groupState.type)) },
                onLongClick = { accept(MainIntent.SelectSample.Expand(type = groupState.type)) }
            )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = groupState.iconId),
                contentScale = ContentScale.Crop,
                contentDescription = groupState.contentDescription,
                colorFilter = ColorFilter.tint(iconColor),
                modifier = Modifier
                    .padding(
                        when {
                            groupState.isShort -> 12.dp
                            else -> 18.dp
                        }
                    )
                    .size(iconSize)
            )
            if (groupState.isExpanded) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    groupState.samples.forEachIndexed { index, sample ->
                        SampleListItem(
                            sample = sample
                        ) {
                            accept(MainIntent.SelectSample.Sample(id = sample.id))
                        }
                        if (index != groupState.samples.lastIndex) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleListItem(
    modifier: Modifier = Modifier,
    sample: SampleUi,
    onClick: () -> Unit
) {
    Text(
        text = sample.name,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
    )
}