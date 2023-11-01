package nay.kirill.samplerecorder.presentation.sampleChooser

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
    val isSelected: Boolean,
    val isExpanded: Boolean = false,
    val isShort: Boolean = false
)

data class SampleUi(
    val id: Int,
    val name: String,
    val isSelected: Boolean
)