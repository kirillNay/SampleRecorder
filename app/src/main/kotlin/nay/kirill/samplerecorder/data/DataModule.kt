package nay.kirill.samplerecorder.data

import linc.com.amplituda.Amplituda
import nay.kirill.samplerecorder.data.repository.LayersRepositoryImpl
import nay.kirill.samplerecorder.data.repository.SampleRepositoryImpl
import nay.kirill.samplerecorder.domain.ResourceManager
import nay.kirill.samplerecorder.domain.repository.LayersRepository
import nay.kirill.samplerecorder.domain.repository.SampleRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val dataModule = module {
    singleOf(::SampleRepositoryImpl).bind<SampleRepository>()
    singleOf(::LayersRepositoryImpl).bind<LayersRepository>()
    singleOf(::ResourceManagerImpl).bind<ResourceManager>()

    factory { Amplituda(get()) }
}