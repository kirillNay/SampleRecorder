package nay.kirill.samplerecorder.player

import nay.kirill.samplerecorder.domain.Player
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val playerModule = module {
    factoryOf(::PlayerImpl).bind<Player>()
}