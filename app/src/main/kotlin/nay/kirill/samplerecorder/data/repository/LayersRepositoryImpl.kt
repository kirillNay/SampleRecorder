package nay.kirill.samplerecorder.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.repository.LayersRepository

internal class LayersRepositoryImpl : LayersRepository {

    private val _layersFlow = MutableStateFlow(listOf<Layer>())
    override val layersFlow: StateFlow<List<Layer>> = _layersFlow.asStateFlow()

    override fun addLayer(layer: Layer) {
        _layersFlow.value = _layersFlow.value.toMutableList().apply {
            val index = indexOfFirst { it.id == layer.id }
            if (index == -1) {
                add(layer)
            } else {
                this[index] = layer
            }
        }
    }

    override fun getLayer(id: Int) = _layersFlow.value[id]

    override fun getLayers(): List<Layer> = _layersFlow.value

    override fun createLayer(): Layer = Layer(
        id = _layersFlow.value.size + 1,
        sample = null,
        speed = 1F,
        volume = 1F
    )
        .also(::addLayer)

}