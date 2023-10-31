package nay.kirill.samplerecorder.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooser
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 26.dp)
    ) {
        SampleChooser(
            state = state.chooserState,
            accept = viewModel::accept
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}

