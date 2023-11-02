package nay.kirill.samplerecorder.data

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleRepository
import nay.kirill.samplerecorder.domain.SampleType

internal class SampleRepositoryImpl : SampleRepository {

    override fun getSamples(): List<Sample> = listOf(
        Sample(
            name = "Deep Purple",
            resourceId = R.raw.deep_purple,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Joe Pass",
            resourceId = R.raw.joe_pass,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Nine Inch Nails",
            resourceId = R.raw.nine_inch_nails,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Ronnie Foster",
            resourceId = R.raw.ronnie_foster,
            type = SampleType.GUITAR
        ),
        Sample(
            name = "Tom Scout",
            resourceId = R.raw.tom_scout,
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
            name = "Avatar",
            resourceId = R.raw.avatar,
            type = SampleType.HORN
        ),
        Sample(
            name = "Charles Aznavour",
            resourceId = R.raw.charles_aznavour,
            type = SampleType.HORN
        ),
        Sample(
            name = "Future",
            resourceId = R.raw.future,
            type = SampleType.HORN
        ),
        Sample(
            name = "J Cole",
            resourceId = R.raw.j_cole,
            type = SampleType.HORN
        ),
        Sample(
            name = "Naruto",
            resourceId = R.raw.narute,
            type = SampleType.HORN
        )
    )

}