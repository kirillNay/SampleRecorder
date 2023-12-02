package nay.kirill.samplerecorder.presentation.main.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.ArtOffsetState
import nay.kirill.samplerecorder.presentation.main.MainIntent
import nay.kirill.samplerecorder.presentation.main.VisializingUIState

@Composable
fun Visualising(
    modifier: Modifier = Modifier,
    state: VisializingUIState.Content,
    accept: (MainIntent.Visualising) -> Unit
) {
    Column (
        modifier = modifier.fillMaxHeight()
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                icon = androidx.appcompat.R.drawable.abc_ic_ab_back_material
            ) {
                accept(MainIntent.Visualising.Back)
            }
            Spacer(modifier = Modifier.width(12.dp))
            TextField(
                value = state.name,
                modifier = Modifier.width(200.dp),
                onValueChange = {
                    accept(MainIntent.Visualising.NameEdit(it))
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                icon = R.drawable.ic_download
            ) {
                accept(MainIntent.Visualising.Back)
            }
        }
        Arts(
            modifier = Modifier
                .height(350.dp)
                .padding(vertical = 48.dp),
            arts = state.arts
        )
        Spacer(modifier = Modifier.weight(1F))
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = state.progress
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = state.currentPosition,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = state.audioDuration,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            Button(
                icon = R.drawable.ic_player_back
            ) {
                accept(MainIntent.Visualising.ToStart)
            }
            Button(
                icon = if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            ) {
                accept(MainIntent.Visualising.Play)
            }
            Button(
                icon = R.drawable.ic_player_forward
            ) {
                accept(MainIntent.Visualising.ToEnd)
            }
        }
    }
}

@Composable
private fun Arts(
    modifier: Modifier = Modifier,
    arts: List<ArtOffsetState>
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val parentWidthPx = constraints.maxWidth
        val parentHeightPx = constraints.maxHeight

        arts.forEach { art ->

            val x by animateDpAsState(
                targetValue = LocalDensity.current.run { (parentWidthPx * art.offsetXScale).toDp() }
            )
            val y by animateDpAsState(
                targetValue =LocalDensity.current.run { (parentHeightPx * art.offsetYScale).toDp() }
            )

            Image(
                modifier = Modifier
                    .offset(
                        x = x,
                        y = y
                    ),
                painter = painterResource(id = art.id),
                contentDescription = "Art form"
            )
        }
    }
}

@Composable
private fun Button(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    accept: () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = accept),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(36.dp),
            painter = painterResource(id = icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@Preview
@Composable
fun VisualisingPreview(

) {
    Visualising(
        state = VisializingUIState.Content(
            audioDuration = "2:02",
            0F,
            currentPosition = "1:02",
            isPlaying = true,
            name = "",
            listOf(),
            
        )
    ) {

    }
}