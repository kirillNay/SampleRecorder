package nay.kirill.samplerecorder.domain.usecase

import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.repository.SampleRepository

class CreateVoiceSampleUseCase(
    private val repository: SampleRepository
) {

    operator fun invoke(): Sample = repository.createVocalSample()

}