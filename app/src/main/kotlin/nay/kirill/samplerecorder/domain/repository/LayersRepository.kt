package nay.kirill.samplerecorder.domain.repository

import nay.kirill.samplerecorder.domain.model.Layer

interface LayersRepository {

    fun addLayer(layer: Layer)

    fun getLayer(id: Int): Layer?

    fun getLayers(): List<Layer>

    fun createLayer(): Layer

}