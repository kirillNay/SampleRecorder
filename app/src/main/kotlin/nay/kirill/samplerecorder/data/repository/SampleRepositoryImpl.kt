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
            name = "Anxiety",
            assetName = "Anxiety.wav",
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Aucustic",
            assetName = "Aucustic.wav",
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Chill",
            assetName = "Chill.wav",
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Smooth",
            assetName = "Smooth.wav",
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Warm",
            assetName = "Warm.wav",
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Airy Whistle",
            assetName = "Airy Whistle.wav",
            type = SampleType.HORN
        ),
        Sample(
            name = "Delightful Echoes",
            assetName = "Delightful Echoes.wav",
            type = SampleType.HORN
        ),
        Sample(
            name = "Flute Ecstasy",
            assetName = "Flute Ecstasy.wav",
            type = SampleType.HORN
        ),
        Sample(
            name = "Freshening Riff",
            assetName = "Freshening Riff.wav",
            type = SampleType.HORN
        ),
        Sample(
            name = "Naruto Flute",
            assetName = "Naruto Flute",
            type = SampleType.HORN
        ),
    )

    override fun getLocalSamples(): List<Sample> = samples

    override fun createVocalSample(): Sample = Sample("Voice", "", SampleType.VOICE).apply { samples.add(this) }

}