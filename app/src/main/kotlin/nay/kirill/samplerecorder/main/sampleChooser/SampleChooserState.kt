package nay.kirill.samplerecorder.main.sampleChooser

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SampleChooserState(
    val sampleGroups: List<SampleGroup>
)

data class SampleGroup(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    val type: SampleType,
    val contentDescription: String,
    val samples: List<SampleUi>
)

enum class SampleType {

    GUITAR, DRUM, TRUMPET

}

data class SampleUi(
    val isDefault: Boolean,
    val id: Int,
    val title: String,
    val isSelected: Boolean
)