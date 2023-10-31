package nay.kirill.samplerecorder.main.sampleChooser

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import nay.kirill.samplerecorder.domain.SampleType

data class SampleChooserUIState(
    val sampleGroups: List<SampleGroupUi>
)

data class SampleGroupUi(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    val type: SampleType,
    val contentDescription: String,
    val samples: List<SampleUi>,
    val isSelected: Boolean
)

data class SampleUi(
    val id: Int,
    val name: String,
    val isSelected: Boolean
)