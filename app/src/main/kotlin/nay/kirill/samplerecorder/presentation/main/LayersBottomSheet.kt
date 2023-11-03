package nay.kirill.samplerecorder.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.theme.SampleRecorderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LayersBottomSheet(
    open: Boolean,
    layers: List<LayerUi>,
    accept: (MainIntent) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (open) {
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
                LazyColumn(
                    modifier = Modifier.padding(bottom = 68.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(layers) { layer ->
                        Layer(layer) {
                            accept(MainIntent.Layers.SelectLayer(layer.id))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                CreateNew {
                    accept(MainIntent.Layers.CreateNew)
                }
            }
        }
    }

}

@Composable
private fun Layer(
    layer: LayerUi,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
            .background(MaterialTheme.colorScheme.primaryContainer)
            .borderInCase(layer.isSelected)
            .clickable(onClick = onClick, enabled = !layer.isSelected),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = layer.name.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
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
private fun BottomSheetPreview() {
    SampleRecorderTheme {
        Scaffold { contentPadding ->
            LayersBottomSheet(
                layers = listOf(
                    LayerUi(
                        1,
                        "Слой 1",
                        false
                    ),
                    LayerUi(
                        2,
                        "Слой 1",
                        false
                    ),
                    LayerUi(
                        3,
                        "Слой 1",
                        false
                    ),
                    LayerUi(
                        4,
                        "Слой 1",
                        false
                    ),
                    LayerUi(
                        5,
                        "Слой 1",
                        false
                    ),
                    LayerUi(
                        6,
                        "Слой 1",
                        false
                    ),
                ),
                open = true
            ) {

            }
        }

    }
}