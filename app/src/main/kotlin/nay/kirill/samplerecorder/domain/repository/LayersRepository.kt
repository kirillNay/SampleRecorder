package nay.kirill.samplerecorder.domain.repository

import kotlinx.coroutines.flow.Flow
import nay.kirill.samplerecorder.domain.model.Layer

interface LayersRepository {

    fun addLayer(layer: Layer)

    fun getLayer(id: Int): Layer?

    fun getLayers(): List<Layer>

    fun createLayer(): Layer

    fun removeLayer(id: Int)

    fun setLayerPlaying(id: Int, isPlaying: Boolean)

    fun clear()

    val layersFlow: Flow<List<Layer>>

}