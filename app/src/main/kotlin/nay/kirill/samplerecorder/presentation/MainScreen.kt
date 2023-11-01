package nay.kirill.samplerecorder.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.presentation.audioController.AudioController
import nay.kirill.samplerecorder.presentation.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.playerController.PlayerController
import nay.kirill.samplerecorder.presentation.playerTimeline.PlayerTimeline
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooser
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(170.dp))
            AudioController(
                modifier = Modifier.weight(1F),
                state = AudioControllerState(0f, 0f)
            )
            Spacer(modifier = Modifier.height(18.dp))
            PlayerTimeline(
                state = state.timeline
            )
            Spacer(modifier = Modifier.height(18.dp))
            PlayerController(
                state = state.playerControllerState,
                accept = viewModel::accept
            )
        }
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

