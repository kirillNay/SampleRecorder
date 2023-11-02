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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.SampleType
import nay.kirill.samplerecorder.presentation.audioController.AudioController
import nay.kirill.samplerecorder.presentation.playerController.PlayerController
import nay.kirill.samplerecorder.presentation.playerTimeline.PlayerTimeline
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooser
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.presentation.sampleChooser.SampleGroupUi
import nay.kirill.samplerecorder.theme.SampleRecorderTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Content(
        state = state,
        accept = viewModel::accept
    )
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
private fun MainScreenPreview() {
    SampleRecorderTheme {
        Content(
            state = MainUIState.Empty(
                chooserState = SampleChooserUIState(
                    sampleGroups = listOf(
                        SampleGroupUi(
                            type = SampleType.GUITAR,
                            titleId = R.string.guitar_sample,
                            iconId = R.drawable.ic_guitar_sample,
                            contentDescription = "Select guitar sample",
                            samples = listOf(),
                            isSelected = true,
                            isExpanded = false,
                            isShort = false
                        ),
                        SampleGroupUi(
                            type = SampleType.DRUM,
                            titleId = R.string.drum_sample,
                            iconId = R.drawable.ic_drums_sample,
                            contentDescription = "Select drums sample",
                            samples = listOf(),
                            isSelected = false,
                            isExpanded = false,
                            isShort = false
                        ),
                        SampleGroupUi(
                            type = SampleType.HORN,
                            titleId = R.string.trumpet_sample,
                            iconId = R.drawable.ic_trumpet_sample,
                            contentDescription = "Select trumpet sample",
                            samples = listOf(),
                            isSelected = false,
                            isExpanded = false,
                            isShort = false
                        )
                    )
                )
            )
        ) {}
    }
}

