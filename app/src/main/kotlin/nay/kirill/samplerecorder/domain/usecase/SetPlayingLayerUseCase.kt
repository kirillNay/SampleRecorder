package nay.kirill.samplerecorder.domain.usecase

import nay.kirill.samplerecorder.domain.repository.LayersRepository

class SetPlayingLayerUseCase(
    private val repository: LayersRepository
) {

    operator fun invoke(id: Int, isPlaying: Boolean) {
        repository.setLayerPlaying(id, isPlaying)
    }

}