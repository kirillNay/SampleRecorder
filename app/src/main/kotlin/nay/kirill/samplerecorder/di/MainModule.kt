package nay.kirill.samplerecorder.di

import nay.kirill.samplerecorder.domain.GetSamplesUseCase
import nay.kirill.samplerecorder.presentation.MainStateConverter
import nay.kirill.samplerecorder.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val mainModule = module {
    viewModelOf(::MainViewModel)
    factoryOf(::MainStateConverter)

    factoryOf(::GetSamplesUseCase)
}