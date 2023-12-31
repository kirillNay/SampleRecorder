package nay.kirill.samplerecorder.presentation.main

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import nay.kirill.samplerecorder.R
import nay.kirill.samplerecorder.domain.ResourceManager
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.model.Sample
import nay.kirill.samplerecorder.domain.model.SampleType
import nay.kirill.samplerecorder.presentation.main.audioController.AudioControllerState
import nay.kirill.samplerecorder.presentation.main.layers.LayersBottomSheetState
import nay.kirill.samplerecorder.presentation.main.playerController.PlayerControllerState
import nay.kirill.samplerecorder.presentation.main.playerTimeline.PlayerTimelineState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleChooserUIState
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleGroupUi
import nay.kirill.samplerecorder.presentation.main.sampleChooser.SampleUi

class MainStateConverter(
    private val resourceManager: ResourceManager,
    private val context: Context
) : (MainState) -> MainUIState {

    override fun invoke(state: MainState): MainUIState {
        return when {
            state.exception != null -> FailureUIState
            state.visualisingState != null -> state.visualisingState(state.visualisingState)
            state.finalRecordState != FinalRecordState.None -> state.finalRecordState()
            state.isVoiceRecording -> SamplingUIState.Recording(
                playerControllerState = PlayerControllerState.EmptySample(
                    layerName = resourceManager.getString(R.string.layer_name, state.currentLayerId),
                    isRecording = true,
                    isFinalRecording = false,
                    isRecordAvailable = isRecordPermissionGranted && state.selectedSample == null
                ),
                layersBottomSheetState = state.layersBottomSheetState()
            )
            state.currentLayer?.sample == null -> SamplingUIState.Empty(
                chooserState = state.chooserState(),
                playerControllerState = PlayerControllerState.EmptySample(
                    layerName = resourceManager.getString(R.string.layer_name, state.currentLayerId),
                    isRecording = false,
                    isFinalRecording = false,
                    isRecordAvailable = isRecordPermissionGranted && state.selectedSample == null
                ),
                layersBottomSheetState = state.layersBottomSheetState()
            )

            else -> SamplingUIState.Sampling(
                chooserState = state.chooserState(),
                playerControllerState = PlayerControllerState.Sampling(
                    playingIcon = if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                    contentDescription = if (state.isPlaying) "Stop" else "Play",
                    layerName = resourceManager.getString(R.string.layer_name, state.currentLayerId),
                    isRecording = false,
                    isFinalRecording = false,
                    isRecordAvailable = isRecordPermissionGranted && state.selectedSample == null
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
                    layerId = state.currentLayerId,
                    volume = state.initialVolumeScale,
                    speed = state.initialSpeedScale,
                    stepSpeedScale = (INITIAL_SPEED_VALUE - MIN_SPEED_VALUE) / (MAX_SPEED_VALUE - MIN_SPEED_VALUE),
                    stepVolumeScale = (INITIAL_VOLUME_VALUE - MIN_VOLUME_VALUE) / (MAX_VOLUME_VALUE - MIN_VOLUME_VALUE),
                    initialSpeedText = "$INITIAL_SPEED_VALUE",
                    initialVolumeText = "$INITIAL_VOLUME_VALUE",
                    maxSpeedText = "$MAX_SPEED_VALUE",
                    maxVolumeText = "$MAX_VOLUME_VALUE"
                ),
                layersBottomSheetState = state.layersBottomSheetState(),
                isVocal = state.currentLayer?.sample?.type == SampleType.VOICE
            )
        }
    }

    private val isRecordPermissionGranted: Boolean get() = ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    private fun MainState.layersBottomSheetState() = LayersBottomSheetState(
        opened = isLayersOpen,
        layers = layers.toUI(selectedId = currentLayerId),
        editAvailable = !isVoiceRecording && finalRecordState != FinalRecordState.Process
    )

    private fun List<Layer>.toUI(selectedId: Int): List<LayerUi> = map { layer ->
        LayerUi(
            id = layer.id,
            name = resourceManager.getString(R.string.layer_name, layer.id),
            isSelected = layer.id == selectedId,
            isPlaying = layer.isPlaying.takeIf { layer.sample != null }
        )
    }

    private fun Int.toDuration(): String = "${this / 60}".padStart(2, '0') + ":" + "${this % 60}".padStart(2, '0')

    private fun MainState.chooserState() = SampleChooserUIState(
        sampleGroups = samples.convertToGroups(currentLayer?.sample?.id, expandedType)
    )

    private fun List<Sample>.convertToGroups(selectedSampleId: Int?, expandedType: SampleType?): List<SampleGroupUi> {
        return groupBy { it.type }.mapNotNull { (type, samples) ->
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

                else -> null
            }
        }
    }

    private fun Sample.toUi(selectedSampleId: Int?): SampleUi = SampleUi(
        id = id,
        name = name,
        isSelected = id == selectedSampleId
    )

    private fun MainState.finalRecordState() = when (finalRecordState) {
        FinalRecordState.Process -> FinalRecordUIState.Recording(
            playerControllerState = PlayerControllerState.EmptySample(
                layerName = resourceManager.getString(R.string.layer_name, currentLayerId),
                isRecording = false,
                isFinalRecording = true,
                isRecordAvailable = false
            ),
            layersBottomSheetState = layersBottomSheetState()
        )
        is FinalRecordState.Complete -> FinalRecordUIState.Complete
        else -> FinalRecordUIState.Saving
    }

    private fun MainState.visualisingState(vState: VisualisingState): MainUIState {
        return VisializingUIState.Content(
            audioDuration = vState.audioDuration.toDuration(),
            progress = vState.progress,
            currentPosition = (vState.audioDuration * vState.progress).toInt().toDuration(),
            isPlaying = vState.isPlaying,
            name = vState.name,
            arts = vState.arts
        )
    }

}