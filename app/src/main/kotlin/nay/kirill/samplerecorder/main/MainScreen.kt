package nay.kirill.samplerecorder.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooser
import nay.kirill.samplerecorder.main.sampleChooser.SampleChooserState

@Composable
internal fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 26.dp)
    ) {
        SampleChooser(
            state = SampleChooserState(listOf(), listOf(), listOf())
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}

