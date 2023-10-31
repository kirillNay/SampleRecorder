package nay.kirill.samplerecorder.di

import nay.kirill.samplerecorder.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val mainModule = module {
    viewModelOf(::MainViewModel)
}