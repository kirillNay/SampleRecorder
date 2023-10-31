package nay.kirill.samplerecorder.domain

interface SampleRepository {

    fun getSamples(): List<Sample>

}