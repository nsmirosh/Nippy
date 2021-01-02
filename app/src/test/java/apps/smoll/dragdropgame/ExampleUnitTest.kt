package apps.smoll.dragdropgame

import apps.smoll.dragdropgame.utils.getRandomXYCoords
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        repeat(100) {
            val widthAndHeight = Pair(500, 500)
            val coords = getRandomXYCoords(widthAndHeight)
            assertTrue(coords.first in shapeWidth / 2 .. widthAndHeight.first - shapeWidth / 2 )
            assertTrue(coords.second in shapeHeight / 2 .. widthAndHeight.second - shapeHeight / 2 )
        }
    }
}