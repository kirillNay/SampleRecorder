package nay.kirill.samplerecorder.domain

interface Player {

    fun create(resourceId: Int)

    fun playLoop()

    fun playOnce()

    fun stop()

}