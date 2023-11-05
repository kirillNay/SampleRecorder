package nay.kirill.samplerecorder.presentation.main.layers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.LayerUi
import nay.kirill.samplerecorder.presentation.main.MainIntent
import nay.kirill.samplerecorder.presentation.main.theme.SampleRecorderTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun LayersBottomSheet(
    state: LayersBottomSheetState,
    accept: (MainIntent) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (state.opened) {
        ModalBottomSheet(
            onDismissRequest = { accept(MainIntent.PlayerController.LayersModal(false)) },
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                val bottomPadding = remember(state.editAvailable) {
                    if (state.editAvailable) 68.dp else 0.dp
                }
                LazyColumn(
                    modifier = Modifier.padding(bottom = bottomPadding),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.layers) { layer ->
                        Layer(
                            modifier = Modifier.animateItemPlacement(),
                            layer = layer,
                            removeAvailable = state.editAvailable,
                            accept = accept
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (state.editAvailable) {
                    CreateNew {
                        accept(MainIntent.Layers.CreateNew)
                    }
                }

            }
        }
    }

}

@Composable
private fun Layer(
    layer: LayerUi,
    removeAvailable: Boolean,
    modifier: Modifier = Modifier,
    accept: (MainIntent.Layers) -> Unit
) {
    @Composable
    fun Modifier.borderInCase(show: Boolean): Modifier = if (show) {
        border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(15))
    } else {
        this
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .borderInCase(layer.isSelected)
            .clickable(
                onClick = {
                    accept(MainIntent.Layers.SelectLayer(layer.id))
                },
                enabled = !layer.isSelected
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = layer.name.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        if (!layer.isSelected && removeAvailable) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        accept(MainIntent.Layers.RemoveLayer(layer.id))
                    }
                    .padding(6.dp),
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = "Remove layer",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )
        }

        if (layer.isPlaying != null) {
            Box(
                modifier = Modifier
                    .padding(end = 64.dp)
                    .size(36.dp)
                    .clip(RoundedCornerShape(15))
                    .align(Alignment.CenterEnd)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { accept(MainIntent.Layers.SetPlaying(layer.id, !layer.isPlaying)) },
                contentAlignment = Alignment.Center,
            ) {
                val playingIcon = remember(layer.isPlaying) {
                    when {
                        layer.isPlaying -> R.drawable.ic_pause
                        else -> R.drawable.ic_play
                    }
                }

                Image(
                    modifier = Modifier
                        .size(36.dp),
                    painter = painterResource(id = playingIcon),
                    contentDescription = "Set playing layer",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
        }
    }
}

@Composable
private fun CreateNew(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    ),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
            .padding(2.dp)
            .clip(RoundedCornerShape(15))
            .background(MaterialTheme.colorScheme.secondaryContainer)

            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.create_new_layer),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun BottomSheetPreview(
    @PreviewParameter(LayersBottomSheetStateProvider::class) state: LayersBottomSheetState
) {
    SampleRecorderTheme {
        LayersBottomSheet(state = state) {}
    }
}