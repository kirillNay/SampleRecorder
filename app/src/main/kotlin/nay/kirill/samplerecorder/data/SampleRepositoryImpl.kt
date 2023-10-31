package nay.kirill.samplerecorder.data

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleRepository
import nay.kirill.samplerecorder.domain.SampleType

internal class SampleRepositoryImpl : SampleRepository {

    override fun getSamples(): List<Sample> = listOf(
        Sample(
            name = "Anxiety",
            resourceId = R.raw.anxiety,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Acoustic",
            resourceId = R.raw.acoustic,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Chill",
            resourceId = R.raw.chill,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Smooth",
            resourceId = R.raw.smooth,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Warm",
            resourceId = R.raw.warm,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Clap",
            resourceId = R.raw.clap,
            type = SampleType.DRUM
        ),
        Sample(
            name = "808",
            resourceId = R.raw.drums_808,
            type = SampleType.DRUM
        ),
        Sample(
            name = "Hihat",
            resourceId = R.raw.hihat,
            type = SampleType.DRUM
        ),
        Sample(
            name = "Kick",
            resourceId = R.raw.kick,
            type = SampleType.DRUM
        ),
        Sample(
            name = "Percussion",
            resourceId = R.raw.percussion,
            type = SampleType.DRUM
        ),
        Sample(
            name = "Air Whistle",
            resourceId = R.raw.airy_whistle,
            type = SampleType.TRUMPET
        ),
        Sample(
            name = "Delightful",
            resourceId = R.raw.delightful_echoes,
            type = SampleType.TRUMPET
        ),
        Sample(
            name = "Ecstasy",
            resourceId = R.raw.flute_ecstasy,
            type = SampleType.TRUMPET
        ),
        Sample(
            name = "Freshening",
            resourceId = R.raw.freshening_riff,
            type = SampleType.TRUMPET
        ),
        Sample(
            name = "Naruto Flute",
            resourceId = R.raw.naruto_flute,
            type = SampleType.TRUMPET
        )
    )

}