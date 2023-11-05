package nay.kirill.samplerecorder.domain.usecase

import kotlinx.coroutines.flow.Flow
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.repository.LayersRepository

class ObserveLayersUseCase(
    private val repository: LayersRepository
) {

    operator fun invoke(): Flow<List<Layer>> = repository.layersFlow

}