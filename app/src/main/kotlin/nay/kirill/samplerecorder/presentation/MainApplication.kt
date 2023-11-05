package nay.kirill.samplerecorder.presentation

import android.app.Application
import nay.kirill.samplerecorder.data.dataModule
import nay.kirill.samplerecorder.presentation.di.mainModule
import nay.kirill.samplerecorder.player.playerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initDI()
    }

    private fun initDI() {
        startKoin {
            androidContext(this@MainApplication)
            modules(
                mainModule,
                dataModule,
                playerModule
            )
        }
    }

}