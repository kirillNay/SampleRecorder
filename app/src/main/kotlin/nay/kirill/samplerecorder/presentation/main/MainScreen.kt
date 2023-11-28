package nay.kirill.samplerecorder.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.presentation.main.theme.SampleRecorderTheme
import nay.kirill.samplerecorder.presentation.main.ui.FinalRecording
import nay.kirill.samplerecorder.presentation.main.ui.Sampling

@Composable
internal fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.uiState.collectAsState()

    SampleRecorderTheme {
            Content(state = state, accept = viewModel::accept)
    }
}

@Composable
private fun Content(
    state: MainUIState,
    accept: (MainIntent) -> Unit
) {
    Scaffold { contentPadding ->
        when (state) {
            is SamplingUIState -> Sampling(
                state = state,
                accept = accept,
                modifier = Modifier.padding(
                    top = 18.dp + contentPadding.calculateTopPadding(),
                    bottom = 18.dp + contentPadding.calculateBottomPadding(),
                    start = 12.dp,
                    end = 12.dp
                )
            )
            is FinalRecordUIState -> FinalRecording(
                state = state,
                accept = accept,
                modifier = Modifier.padding(
                    top = 18.dp + contentPadding.calculateTopPadding(),
                    bottom = 18.dp + contentPadding.calculateBottomPadding(),
                    start = 12.dp,
                    end = 12.dp
                )
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPreview(
    @PreviewParameter(MainUIStateProvider::class) state: SamplingUIState
) {
    Content(state = state) {}
}

