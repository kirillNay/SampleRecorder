package nay.kirill.samplerecorder.domain

interface ResourceManager {

    fun getString(id: Int, vararg formatArgs: Any): String

}