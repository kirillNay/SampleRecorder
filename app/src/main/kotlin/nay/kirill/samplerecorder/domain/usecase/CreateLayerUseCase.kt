package nay.kirill.samplerecorder.domain.usecase

import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.repository.LayersRepository

class CreateLayerUseCase(
    private val repository: LayersRepository
) {

    operator fun invoke(): Layer = repository.createLayer()

}