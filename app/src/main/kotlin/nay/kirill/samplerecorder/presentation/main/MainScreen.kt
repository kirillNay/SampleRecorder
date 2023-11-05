package nay.kirill.samplerecorder.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.audioController.AudioController
import nay.kirill.samplerecorder.presentation.main.layers.LayersBottomSheet
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerController
import nay.kirill.samplerecorder.presentation.main.playerTimeline.PlayerTimeline
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleChooser
import nay.kirill.samplerecorder.presentation.main.theme.SampleRecorderTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
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
        LayersBottomSheet(
            state = state.layersBottomSheetState,
            accept = accept
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 18.dp + contentPadding.calculateTopPadding(),
                    bottom = 18.dp + contentPadding.calculateBottomPadding(),
                    start = 12.dp,
                    end = 12.dp
                ),
            color = MaterialTheme.colorScheme.background
        ) {
            when (state) {
                is MainUIState.Empty -> {
                    Empty(state, accept)
                    SampleChooser(
                        state = state.chooserState,
                        accept = accept
                    )
                }
                is MainUIState.Sampling -> {
                    Sampling(state, accept)
                    if (!state.isVocal) {
                        SampleChooser(
                            state = state.chooserState,
                            accept = accept
                        )
                    }
                }
                is MainUIState.FinalRecording -> {
                    FinalRecording(state, accept)
                }
                is MainUIState.Recording -> {
                    Recording(state, accept)
                }
            }

        }
    }
}

@Composable
private fun Recording(
    state: MainUIState.Recording,
    accept: (MainIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(250.dp))
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_recording))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

        LottieAnimation(
            modifier = Modifier.size(96.dp),
            composition = composition,
            progress = { progress },
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.record_on_process),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.weight(1F))
        PlayerController(
            state = state.playerControllerState,
            accept = accept
        )
    }
}

@Composable
private fun FinalRecording(
    state: MainUIState.FinalRecording,
    accept: (MainIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(250.dp))
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.final_recording))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

        LottieAnimation(
            modifier = Modifier.size(96.dp),
            composition = composition,
            progress = { progress },
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.final_record_on_process),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.weight(1F))
        PlayerController(
            state = state.playerControllerState,
            accept = accept
        )
    }
}

@Composable
private fun Empty(
    state: MainUIState.Empty,
    accept: (MainIntent) -> Unit
) {
    Column(
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
        PlayerController(
            state = state.playerControllerState,
            accept = accept
        )
    }
}

@Composable
private fun Sampling(
    state: MainUIState.Sampling,
    accept: (MainIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(state.isVocal) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(250.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_microphone),
                    contentDescription = "Audio wave",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.vocal_sample),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.weight(1F))
                PlayerController(
                    state = state.playerControllerState,
                    accept = accept
                )
            }
        } else {
            Spacer(modifier = Modifier.height(170.dp))
            AudioController(
                modifier = Modifier.weight(1F),
                state = state.audioControllerState,
                accept = accept
            )
        }
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPreview(
    @PreviewParameter(MainUIStateProvider::class) state: MainUIState
) {
    Content(state = state) {}
}

