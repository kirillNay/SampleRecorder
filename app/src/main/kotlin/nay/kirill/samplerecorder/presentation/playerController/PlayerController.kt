package nay.kirill.samplerecorder.presentation.playerController

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.MainIntent

@Composable
internal fun PlayerController(
    modifier: Modifier = Modifier,
    state: PlayerControllerState,
    accept: (MainIntent.PlayerController) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
    ){
        Box(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 24.dp)
        ) {
            LayerChooser(
                modifier = Modifier.fillMaxSize(),
                name = state.layerName
            ) {
                accept(MainIntent.PlayerController.LayersModal(open = true))
            }
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(15))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { accept(MainIntent.PlayerController.OnPlayButton) },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(36.dp),
                painter = painterResource(id = state.playingIcon),
                contentDescription = state.contentDescription,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }
        Box(
            modifier = Modifier
                .weight(1F, true)
                .clip(RoundedCornerShape(15))
        ) {

        }
    }
}

@Composable
internal fun LayerChooser(
    modifier: Modifier,
    name: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
@Preview
private fun PlayerControllerPreview() {
    PlayerController(
        state = PlayerControllerState(
            playingIcon = R.drawable.ic_play,
            contentDescription = "",
            layerName = "Слой 1"
        )
    ) {

    }
}