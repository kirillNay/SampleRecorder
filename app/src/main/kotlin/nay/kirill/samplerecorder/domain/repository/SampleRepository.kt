package nay.kirill.samplerecorder.domain.repository

import nay.kirill.samplerecorder.domain.model.Sample

interface SampleRepository {

    fun getSamples(): List<Sample>

}