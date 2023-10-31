package nay.kirill.samplerecorder.domain

class GetSamplesUseCase(
        private val repository: SampleRepository
) {

    operator fun invoke(): List<Sample> = repository.getSamples()

}