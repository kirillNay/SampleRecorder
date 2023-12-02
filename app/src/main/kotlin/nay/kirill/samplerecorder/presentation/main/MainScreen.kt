package nay.kirill.samplerecorder.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.theme.SampleRecorderTheme
import nay.kirill.samplerecorder.presentation.main.ui.FinalRecording
import nay.kirill.samplerecorder.presentation.main.ui.Sampling
import nay.kirill.samplerecorder.presentation.main.ui.Visualising

@Composable
internal fun MainScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.uiState.collectAsState()

    val accept = remember<(MainIntent) -> Unit> {
        { viewModel.accept(it) }
    }

    SampleRecorderTheme {
            Content(
                state = state,
                accept = accept
            )
    }
}

@Composable
private fun Content(
    state: MainUIState,
    accept: (MainIntent) -> Unit
) {
    Scaffold { contentPadding ->
        fun Modifier.applyContentPadding() = padding(
            top = 18.dp + contentPadding.calculateTopPadding(),
            bottom = 18.dp + contentPadding.calculateBottomPadding(),
            start = 12.dp,
            end = 12.dp
        )

        when (state) {
            is SamplingUIState -> Sampling(
                state = state,
                accept = accept,
                modifier = Modifier.applyContentPadding()
            )
            is FinalRecordUIState -> FinalRecording(
                state = state,
                accept = accept,
                modifier = Modifier.applyContentPadding()
            )
            is FailureUIState -> Failure(
                modifier = Modifier.applyContentPadding(),
                accept = accept
            )
            is VisializingUIState.Content -> Visualising(
                modifier = Modifier.applyContentPadding(),
                state = state,
                accept = accept
            )
        }

    }
}

@Composable
private fun Failure(
    modifier: Modifier = Modifier,
    accept: (MainIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(250.dp))
        Text(
            text = stringResource(id = R.string.common_failure_message),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(96.dp))
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(15))
                .height(48.dp)
                .width(160.dp)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { accept(MainIntent.FinalRecord.Reset) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.reply_button).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
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

