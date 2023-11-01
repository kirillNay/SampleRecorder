package nay.kirill.samplerecorder.presentation.playerController

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
    accept: (MainIntent.Player) -> Unit
) {
    fun Modifier.clickableIfEnabled(
        enabled: Boolean
    ) = if (enabled) {
        clickable { accept(MainIntent.Player.OnPlayButton) }
    } else {
        this
    }

    Column (
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(15))
                .background(
                    if (state.isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
                .clickableIfEnabled(enabled = state.isEnabled),
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
    }
}

@Composable
@Preview
private fun PlayerControllerPreview() {
    PlayerController(
        state = PlayerControllerState(
            playingIcon = R.drawable.ic_play,
            contentDescription = "",
            isEnabled = true
        )
    ) {

    }
}