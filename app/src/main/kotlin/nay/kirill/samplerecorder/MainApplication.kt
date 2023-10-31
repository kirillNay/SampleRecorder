package nay.kirill.samplerecorder

import android.app.Application
import nay.kirill.samplerecorder.di.mainModule
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
            modules(mainModule)
        }
    }

}