package nay.kirill.samplerecorder.presentation.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.presentation.main.FinalRecordUIState
import nay.kirill.samplerecorder.presentation.main.MainIntent
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerController

@Composable
internal fun FinalRecording(
    state: FinalRecordUIState,
    accept: (MainIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is FinalRecordUIState.Recording -> Process(state, accept, modifier)
        is FinalRecordUIState.Saving -> Saving(modifier)
        is FinalRecordUIState.Complete -> Complete(modifier, accept)
    }
}

@Composable
private fun Process(
    state: FinalRecordUIState.Recording,
    accept: (MainIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
            color = MaterialTheme.colorScheme.surfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1F))
        PlayerController(
            state = state.playerControllerState,
            accept = accept
        )
    }
}

@Composable
private fun Saving(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
            text = stringResource(id = R.string.final_record_on_saving),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Complete(
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
            text = stringResource(id = R.string.final_record_saved),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(96.dp))
        Button(
            name = stringResource(R.string.final_record_share_button),
        ) {
            accept(MainIntent.FinalRecord.Share)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            name = stringResource(R.string.final_record_reset_button),
        ) {
            accept(MainIntent.FinalRecord.Reset)
        }
    }
}

@Composable
private fun Button(
    modifier: Modifier = Modifier,
    name: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15))
            .height(48.dp)
            .width(160.dp)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}