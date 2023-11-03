package nay.kirill.samplerecorder.player

import nay.kirill.samplerecorder.domain.Player
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val playerModule = module {
    singleOf(::PlayerImpl).bind<Player>()
}