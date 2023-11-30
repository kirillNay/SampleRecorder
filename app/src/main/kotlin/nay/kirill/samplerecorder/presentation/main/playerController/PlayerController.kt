package nay.kirill.samplerecorder.presentation.main.playerController

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.MainIntent

@Composable
internal fun PlayerController(
    state: PlayerControllerState,
    accept: (MainIntent.PlayerController) -> Unit
) {
    val acceptInternal = remember<(MainIntent.PlayerController) -> Unit> {
        { accept(it) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .weight(1F)
                .height(48.dp)
                .padding(horizontal = 24.dp)
        ) {
            LayerChooser(
                modifier = Modifier.fillMaxSize(),
                name = state.layerName
            ) {
                acceptInternal(MainIntent.PlayerController.LayersModal(open = true))
            }
        }
        if (state is PlayerControllerState.Sampling) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { acceptInternal(MainIntent.PlayerController.OnPlayButton) },
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

        Row(
            modifier = Modifier
                .weight(1F),
            horizontalArrangement = Arrangement.Center
        ) {
            RecordVoice(
                state = state,
                acceptInternal = acceptInternal
            )
            Spacer(Modifier.width(12.dp))
            RecordFinal(
                state = state,
                acceptInternal = acceptInternal
            )
        }
    }
}

@Composable
private fun RecordVoice(
    modifier: Modifier = Modifier,
    state: PlayerControllerState,
    acceptInternal: (MainIntent.PlayerController) -> Unit
) {
    val size by animateDpAsState(
        targetValue = when {
            state.isRecording -> 64.dp
            else -> 48.dp
        }
    )
    val iconSize by animateDpAsState(
        targetValue = when {
            state.isRecording -> 48.dp
            else -> 36.dp
        }
    )

    if (state.isRecordAvailable && !state.isFinalRecording) {
        Box(
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(15))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { acceptInternal(MainIntent.PlayerController.OnRecord) },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_microphone),
                contentDescription = "Recording",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

@Composable
private fun RecordFinal(
    modifier: Modifier = Modifier,
    state: PlayerControllerState,
    acceptInternal: (MainIntent.PlayerController) -> Unit
) {
    val size by animateDpAsState(
        targetValue = when {
            state.isFinalRecording -> 64.dp
            else -> 48.dp
        }
    )
    val iconSize by animateDpAsState(
        targetValue = when {
            state.isFinalRecording -> 48.dp
            else -> 36.dp
        }
    )

    if (!state.isRecording) {
        Box(
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(15))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { acceptInternal(MainIntent.PlayerController.OnFinalRecord) },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(iconSize)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_record),
                contentDescription = "Recording",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

@Composable
private fun LayerChooser(
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
private fun PlayerControllerPreview(
    @PreviewParameter(PlayerControllerStateProvider::class) state: PlayerControllerState
) {
    PlayerController(
        state = state
    ) {

    }
}