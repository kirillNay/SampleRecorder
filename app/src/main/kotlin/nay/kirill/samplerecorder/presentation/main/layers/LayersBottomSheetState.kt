package nay.kirill.samplerecorder.presentation.main.layers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import nay.kirill.samplerecorder.presentation.main.LayerUi

data class LayersBottomSheetState(
    val opened: Boolean,
    val layers: List<LayerUi>,
    val editAvailable: Boolean
)

internal class LayersBottomSheetStateProvider : PreviewParameterProvider<LayersBottomSheetState> {

    override val values: Sequence<LayersBottomSheetState> = sequenceOf(
        LayersBottomSheetState(
            opened = false,
            layers = emptyList(),
            editAvailable = false
        ),
        LayersBottomSheetState(
            opened = true,
            layers = listOf(
                LayerUi(1, "Слой 1", false),
                LayerUi(2, "Слой 1", false),
                LayerUi(3, "Слой 1", false)
            ),
            editAvailable = false
        ),
        LayersBottomSheetState(
            opened = true,
            layers = listOf(
                LayerUi(1, "Слой 1", false),
                LayerUi(2, "Слой 1", false),
                LayerUi(3, "Слой 1", false)
            ),
            editAvailable = true
        )
    )

}