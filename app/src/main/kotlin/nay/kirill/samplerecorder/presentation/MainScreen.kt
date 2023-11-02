package nay.kirill.samplerecorder.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.audioController.AudioController
import nay.kirill.samplerecorder.presentation.playerController.LayerChooser
import nay.kirill.samplerecorder.presentation.playerController.PlayerController
import nay.kirill.samplerecorder.presentation.playerTimeline.PlayerTimeline
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooser
import nay.kirill.samplerecorder.theme.SampleRecorderTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    SampleRecorderTheme {
        Scaffold{  contentPadding ->
            LayersBottomSheet(
                open = state.isLayersModalOpen,
                accept = viewModel::accept
            )

            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Content(
                    state = state,
                    accept = viewModel::accept
                )
            }
        }

    }
}

@Composable
private fun Content(
    state: MainUIState,
    accept: (MainIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 12.dp)
    ) {
        when (state) {
            is MainUIState.Empty -> Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(250.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_audio_wave),
                    contentDescription = "Audio wave",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.no_sample_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.weight(1F))
                LayerChooser(
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp),
                    name = state.layerName
                ) {
                    accept(MainIntent.PlayerController.LayersModal(open = true))
                }
            }

            is MainUIState.Sampling -> Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(170.dp))
                AudioController(
                    modifier = Modifier.weight(1F),
                    state = state.audioControllerState,
                    accept = accept
                )
                Spacer(modifier = Modifier.height(18.dp))
                PlayerTimeline(
                    state = state.timeline,
                    accept = accept
                )
                Spacer(modifier = Modifier.height(18.dp))
                PlayerController(
                    state = state.playerControllerState,
                    accept = accept
                )
            }
        }

        SampleChooser(
            state = state.chooserState,
            accept = accept
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPreview(
    @PreviewParameter(MainUIStateProvider::class) state: MainUIState
) {
    SampleRecorderTheme {
        Content(state = state) {}
    }
}

