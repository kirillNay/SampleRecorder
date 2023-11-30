package nay.kirill.samplerecorder.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import nay.kirill.samplerecorder.presentation.main.MainEvent
import nay.kirill.samplerecorder.presentation.main.MainIntent
import nay.kirill.samplerecorder.presentation.main.MainScreen
import nay.kirill.samplerecorder.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onPause() {
        super.onPause()
        viewModel.accept(MainIntent.Lifecycle.OnPause)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LaunchedEffect(key1 = true) {
                viewModel.eventsFlow.collect { event ->
                    when (event) {
                        is MainEvent.ShareFile -> {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "audio/wav"
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(event.fileDirectory))
                            startActivity(Intent.createChooser(intent, "Share recorded file!"))
                        }
                        is MainEvent.FailureToast -> {
                            Toast.makeText(this@MainActivity, "Ошибка: " + event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            MainScreen(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.accept(MainIntent.Lifecycle.OnResume)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), 0);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) viewModel.accept(MainIntent.Lifecycle.OnDestroy)
    }
}
