package nay.kirill.samplerecorder.data

import nay.kirill.samplerecorder.domain.SampleRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val dataModule = module {
    factoryOf(::SampleRepositoryImpl).bind<SampleRepository>()
}