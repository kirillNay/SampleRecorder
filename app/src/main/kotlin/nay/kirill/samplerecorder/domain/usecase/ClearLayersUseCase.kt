package nay.kirill.samplerecorder.domain.usecase

import nay.kirill.samplerecorder.domain.repository.LayersRepository

class ClearLayersUseCase(
    private val repository: LayersRepository
) {

    operator fun invoke() {
        repository.clear()
    }

}