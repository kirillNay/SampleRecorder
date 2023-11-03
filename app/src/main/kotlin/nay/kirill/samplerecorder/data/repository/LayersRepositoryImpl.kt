package nay.kirill.samplerecorder.data.repository

import nay.kirill.samplerecorder.domain.model.Layer
import nay.kirill.samplerecorder.domain.repository.LayersRepository

internal class LayersRepositoryImpl : LayersRepository {

    private val layers = mutableMapOf<Int,Layer>()

    override fun addLayer(layer: Layer) {
        layers[layer.id] = layer
    }

    override fun getLayer(id: Int) = layers[id]

    override fun getLayers(): List<Layer> = layers.values.toList()

    override fun createLayer(): Layer = Layer(
        id = layers.size + 1,
        sample = null,
        speed = 1F,
        volume = 1F
    )

}