package nay.kirill.samplerecorder.presentation.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.MainIntent
import nay.kirill.samplerecorder.presentation.main.SamplingUIState
import nay.kirill.samplerecorder.presentation.main.audioController.AudioController
import nay.kirill.samplerecorder.presentation.main.layers.LayersBottomSheet
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerController
import nay.kirill.samplerecorder.presentation.main.playerTimeline.PlayerTimeline
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleChooser

@Composable
internal fun Sampling(
    state: SamplingUIState,
    modifier: Modifier = Modifier,
    accept: (MainIntent) -> Unit
) {
    LayersBottomSheet(
        state = state.layersBottomSheetState,
        accept = accept
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (state) {
            is SamplingUIState.Empty -> {
                Empty(state, accept)
                SampleChooser(
                    state = state.chooserState,
                    accept = accept
                )
            }
            is SamplingUIState.Sampling -> {
                SelectedSample(state, accept)
                if (!state.isVocal) {
                    SampleChooser(
                        state = state.chooserState,
                        accept = accept
                    )
                }
            }
            is SamplingUIState.Recording -> {
                Recording(state, accept)
            }
        }

    }
}

@Composable
private fun Recording(
    state: SamplingUIState.Recording,
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
private fun Empty(
    state: SamplingUIState.Empty,
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
private fun SelectedSample(
    state: SamplingUIState.Sampling,
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