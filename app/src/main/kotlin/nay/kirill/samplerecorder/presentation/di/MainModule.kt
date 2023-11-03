package nay.kirill.samplerecorder.presentation.di

import nay.kirill.samplerecorder.domain.usecase.CreateLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.ObserveLayersUseCase
import nay.kirill.samplerecorder.domain.usecase.SaveLayerUseCase
import nay.kirill.samplerecorder.domain.usecase.GetSamplesUseCase
import nay.kirill.samplerecorder.domain.usecase.RemoveLayerUseCase
import nay.kirill.samplerecorder.presentation.main.MainStateConverter
import nay.kirill.samplerecorder.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val mainModule = module {
    viewModelOf(::MainViewModel)
    factoryOf(::MainStateConverter)

    factoryOf(::RemoveLayerUseCase)
    factoryOf(::GetSamplesUseCase)
    factoryOf(::SaveLayerUseCase)
    factoryOf(::CreateLayerUseCase)
    factoryOf(::ObserveLayersUseCase)
}