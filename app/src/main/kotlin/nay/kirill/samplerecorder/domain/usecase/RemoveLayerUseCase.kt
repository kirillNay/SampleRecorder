package nay.kirill.samplerecorder.domain.usecase

import nay.kirill.samplerecorder.domain.repository.LayersRepository

class RemoveLayerUseCase(
    private val repository: LayersRepository
) {

    operator fun invoke(id: Int) {
        repository.removeLayer(id)
    }

}