package nay.kirill.samplerecorder.player

import junit.framework.TestCase.assertEquals
import org.junit.Test

internal class AmplitudeConverterTest {

    @Test
    fun `Provided list converted with the same size`() {
        val list = listOf(3,6,5,9,8,7,4)
        val actual = AmplitudeConverter.map(7, list)

        assertEquals(listOf(0.0F,0.5F,1/3F,1F,5/6F,2/3F,1/6F), actual)
    }

    @Test
    fun `Provided list converted with the less size`() {
        val list = listOf(3,6,5,9,8,7)
        val actual = AmplitudeConverter.map(5, list)

        assertEquals(listOf(0.0F,0.5F,1/3F,5/6F,2/3F), actual)
    }

    @Test
    fun `Provided list converted with the more size`() {
        val list = listOf(3,6,5,9,8,7)
        val actual = AmplitudeConverter.map(7, list)

        assertEquals(listOf(0.0F,0.5F,1/3F,1F,5/6F + (1F-5/6F)/2F,5/6F,2/3F), actual)
    }

    @Test
    fun `Provided list converted with the d more size`() {
        val list = listOf(27, 37, 27, 22, 19, 18, 18, 18, 32, 28, 24, 21, 18, 15, 12, 12, 10, 8, 7, 8, 9, 9, 9, 9, 9, 9, 10, 10, 12, 21, 6, 2, 1, 1, 0, 1, 1, 16, 15, 15, 12, 12, 11, 12, 11, 12, 12, 12, 12, 12, 0, 12, 11, 10, 9, 8, 8, 8, 20, 19, 20, 18, 15, 15, 17, 18, 12, 9, 11, 9, 9, 11, 17, 15, 15, 17, 9, 10, 10, 9, 22, 24, 20, 14, 11, 6, 8, 10, 9, 7, 6, 6, 6, 5, 4, 4, 3, 3, 3, 3, 2, 21, 17, 15, 15, 13, 13, 12, 13, 13, 14, 14, 14, 12, 4, 29, 31, 23, 18, 16, 11, 14, 18, 33, 29, 25, 24, 21, 18, 21, 10, 10, 6, 7, 8, 8, 8, 9, 9, 9, 9, 10, 10, 10, 23, 7, 0, 0, 0, 0, 1, 14, 18, 15, 14, 13, 13, 13, 14, 14, 14, 15, 14, 13, 13, 12, 14, 12, 11, 10, 10, 10, 7, 22, 18, 16, 15, 16, 18, 16, 15, 12, 12, 12, 11, 12, 11, 13, 13, 14, 12, 14, 13, 5, 16, 25, 21, 17, 13, 12, 11, 11, 11, 11, 10, 9, 8, 7, 7, 7, 7, 6, 5, 4, 4, 4, 22, 18, 16, 16, 15, 16, 14, 14, 15, 14, 14, 14, 15, 21, 24, 31, 25, 22, 19, 18, 16, 22, 31, 25, 21, 21, 20, 13, 14, 9, 8, 9, 9, 10, 10, 11, 11, 11, 11, 11, 12, 12, 14, 14, 1, 1, 2, 1, 0, 0, 1, 19, 15, 14, 14, 14, 14, 14, 14, 14, 14, 13, 12, 12, 4, 14, 13, 12, 11, 10, 10, 10, 15, 17, 22, 17, 16, 15, 14, 15, 15, 12, 9, 10, 11, 8, 10, 14, 13, 13, 10, 9, 5, 2, 27, 21, 20, 16, 15, 13, 13, 13, 14, 13, 11, 10, 9, 8, 7, 7, 6, 5, 5, 5, 5, 24, 21, 19, 17, 18, 16, 16, 16, 15, 14, 13, 12, 8, 25, 47, 40, 43, 30, 32, 13, 2, 1, 1, 1, 1, 2, 15, 30, 52, 29, 28, 29, 23, 27, 17, 20, 17, 17, 17, 14, 15, 11, 14, 4, 2, 1, 0, 0, 0, 0, 22, 18, 17, 15, 14, 14, 15, 15, 14, 15, 14, 14, 14, 24, 19, 15, 15, 12, 12, 6, 19, 51, 41, 28, 25, 25, 22, 9, 1, 0, 0, 1, 1, 1, 15, 47, 42, 21, 23, 23, 18, 17, 19, 16, 15, 14, 12, 11, 11, 14, 17, 14, 11, 11, 11, 11, 12, 15, 15, 15, 13, 12, 12, 7, 1, 1, 13, 13, 12, 10, 10, 11, 11, 9, 9, 11, 10, 10, 2)
        val actual = AmplitudeConverter.map(100, list)

        assertEquals(100, actual.size)
    }

}