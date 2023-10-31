package nay.kirill.samplerecorder.data

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.Sample
import nay.kirill.samplerecorder.domain.SampleRepository
import nay.kirill.samplerecorder.domain.SampleType

internal class SampleRepositoryImpl : SampleRepository {

    override fun getSamples(): List<Sample> = listOf(
        Sample(
            id = idCounter++,
            name = "Гитара 1",
            resourceId = R.raw.guitar_1,
            type = SampleType.GUITAR
        ),
        Sample(
            id = idCounter++,
            name = "Гитара 2",
            resourceId = R.raw.guitar_2,
            type = SampleType.GUITAR
        ),
        Sample(
            id = idCounter++,
            name = "Гитара 3",
            resourceId = R.raw.guitar_3,
            type = SampleType.GUITAR
        ),
        Sample(
            id = idCounter++,
            name = "Гитара 4",
            resourceId = R.raw.guitar_4,
            type = SampleType.GUITAR
        ),
        Sample(
            id = idCounter++,
            name = "Ударные 1",
            resourceId = R.raw.drums_1,
            type = SampleType.DRUM
        ),
        Sample(
            id = idCounter++,
            name = "Ударные 2",
            resourceId = R.raw.drums_2,
            type = SampleType.DRUM
        ),
        Sample(
            id = idCounter++,
            name = "Ударные 3",
            resourceId = R.raw.drums_3,
            type = SampleType.DRUM
        ),
        Sample(
            id = idCounter++,
            name = "Ударные 4",
            resourceId = R.raw.drums_4,
            type = SampleType.DRUM
        ),
        Sample(
            id = idCounter++,
            name = "Ударные 5",
            resourceId = R.raw.drums_5,
            type = SampleType.DRUM
        ),
        Sample(
            id = idCounter++,
            name = "Духовые 1",
            resourceId = R.raw.trumpet_1,
            type = SampleType.TRUMPET
        ),
        Sample(
            id = idCounter++,
            name = "Духовые 2",
            resourceId = R.raw.trumpet_2,
            type = SampleType.TRUMPET
        ),
        Sample(
            id = idCounter++,
            name = "Духовые 3",
            resourceId = R.raw.trumpet_3,
            type = SampleType.TRUMPET
        )
    )

    companion object {

        private var idCounter: Int = 0

    }

}