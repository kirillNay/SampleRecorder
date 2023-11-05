package nay.kirill.samplerecorder.data.repository

import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.repository.SampleRepository
import nay.kirill.samplerecorder.domain.model.SampleType

internal class SampleRepositoryImpl : SampleRepository {

    private val samples = mutableListOf(
        Sample(
            name = "Clap",
            assetName = "clap.wav",
            type = SampleType.DRUM
        ),
        Sample(
            name = "808",
            assetName = "drums_808.wav",
            type = SampleType.DRUM
        ),
        Sample(
            name = "Hihat",
            assetName = "hihat.wav",
            type = SampleType.DRUM
        ),
        Sample(
            name = "Kick",
            assetName = "kick.wav",
            type = SampleType.DRUM
        ),
        Sample(
            name = "Percussion",
            assetName = "percussion.wav",
            type = SampleType.DRUM
        )
    )

    override fun getLocalSamples(): List<Sample> = samples

    override fun createVocalSample(): Sample = Sample("Voice", "", SampleType.VOICE).apply { samples.add(this) }

}