package nay.kirill.samplerecorder.player

object AmplitudeConverter {

    fun map(size: Int, list: List<Int>): List<Float> {
        val max = list.max()
        val min = list.min()
        val result = list.map { value -> (value - min).toFloat() / (max - min).toFloat() }.toMutableList()

        var step = result.size / 2
        var index = step
        if (result.size < size) {
            while (result.size < size) {
                result.add(index + 1, (result[index] + result[index + 1]) / 2F)

                index += step
                if (index >= result.size - 1) {
                    index = 0
                    step /= 2
                }
            }
        } else {
            while (result.size > size) {
                result.removeAt(index)

                index += step
                if (index >= result.size - 1) {
                    index = 0
                    step /= 2
                }
            }
        }

        return result.toList()
    }

}