package nay.kirill.samplerecorder.presentation.main

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LayersBottomSheet(
    open: Boolean,
    accept: (MainIntent) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    if (open) {
        ModalBottomSheet(
            onDismissRequest = { accept(MainIntent.PlayerController.LayersModal(false)) },
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {

        }
    }


}