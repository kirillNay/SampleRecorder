package nay.kirill.samplerecorder.presentation.main

import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.ResourceManager
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.model.SampleType
import nay.kirill.samplerecorder.presentation.main.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.main.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleGroupUi
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleUi

class MainStateConverter(
    private val resourceManager: ResourceManager
) : (MainState) -> MainUIState {

    override fun invoke(state: MainState): MainUIState {
        return when (state.currentLayer.sample) {
            null -> MainUIState.Empty(
                chooserState = state.chooserState(),
                isLayersModalOpen = state.isLayersOpen,
                layers = state.layers.toUI(selectedId = state.currentLayer.id),
                playerControllerState = PlayerControllerState.EmptySample(
                    layerName = resourceManager.getString(R.string.layer_name, state.currentLayer.id),
                    isRecording = state.isRecording
                )
            )

            else -> MainUIState.Sampling(
                chooserState = state.chooserState(),
                layers = state.layers.toUI(selectedId = state.currentLayer.id),
                playerControllerState = PlayerControllerState.Sampling(
                    playingIcon = if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                    contentDescription = if (state.isPlaying) "Stop" else "Play",
                    layerName = resourceManager.getString(R.string.layer_name, state.currentLayer.id),
                    isRecording = state.isRecording
                ),
                timeline = state.amplitude?.let {
                    PlayerTimelineState.Data(
                        amplitude = it,
                        progress = state.progress,
                        duration = state.duration.toDuration(),
                        currentPosition = (state.duration * state.progress).toInt().toDuration()
                    )
                } ?: PlayerTimelineState.Empty,
                audioControllerState = AudioControllerState(
                    initialVolume = state.initialVolumeScale,
                    initialSpeed = state.initialSpeedScale,
                    initialSpeedText = "$INITIAL_SPEED_VALUE",
                    initialVolumeText = "$INITIAL_VOLUME_VALUE",
                    maxSpeedText = "$MAX_SPEED_VALUE",
                    maxVolumeText = "$MAX_VOLUME_VALUE"
                ),
                isLayersModalOpen = state.isLayersOpen
            )
        }
    }

    private fun List<Layer>.toUI(selectedId: Int): List<LayerUi> = map {
        LayerUi(
            id = it.id,
            name = resourceManager.getString(R.string.layer_name, it.id),
            isSelected = it.id == selectedId
        )
    }

    private fun Int.toDuration(): String = "${this / 1_000 / 60}".padStart(2, '0') + ":" + "${this / 1_000 % 60}".padStart(2, '0')

    private fun MainState.chooserState() = SampleChooserUIState(
        sampleGroups = samples.convertToGroups(currentLayer.sample?.id, expandedType)
    )

    private fun List<Sample>.convertToGroups(selectedSampleId: Int?, expandedType: SampleType?): List<SampleGroupUi> {
        return groupBy { it.type }.map { (type, samples) ->
            when (type) {
                SampleType.GUITAR -> SampleGroupUi(
                    type = SampleType.GUITAR,
                    titleId = R.string.guitar_sample,
                    iconId = R.drawable.ic_guitar_sample,
                    contentDescription = "Select guitar sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId },
                    isExpanded = expandedType == type,
                    isShort = expandedType != null && expandedType != type
                )

                SampleType.DRUM -> SampleGroupUi(
                    type = SampleType.DRUM,
                    titleId = R.string.drum_sample,
                    iconId = R.drawable.ic_drums_sample,
                    contentDescription = "Select drum sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId },
                    isExpanded = expandedType == type,
                    isShort = expandedType != null && expandedType != type
                )

                SampleType.HORN -> SampleGroupUi(
                    type = SampleType.HORN,
                    titleId = R.string.trumpet_sample,
                    iconId = R.drawable.ic_trumpet_sample,
                    contentDescription = "Select guitar sample",
                    samples = samples.map { it.toUi(selectedSampleId) },
                    isSelected = samples.any { it.id == selectedSampleId },
                    isExpanded = expandedType == type,
                    isShort = expandedType != null && expandedType != type
                )
            }
        }
    }

    private fun Sample.toUi(selectedSampleId: Int?): SampleUi = SampleUi(
        id = id,
        name = name,
        isSelected = id == selectedSampleId
    )

}