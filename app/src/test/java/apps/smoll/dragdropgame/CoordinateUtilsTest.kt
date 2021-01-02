package apps.smoll.dragdropgame

import apps.smoll.dragdropgame.utils.getRandomXYCoords
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoordinateUtilsTest {

    @Test
    fun getRandomXYCoordinates_withValidParameters_isWithinProperRange() {
        repeat(10000) {
            val widthAndHeight = Pair(500, 500)
            val coords = getRandomXYCoords(widthAndHeight)
            assertTrue(coords.first in shapeWidth / 2 .. widthAndHeight.first - shapeWidth / 2 )
            assertTrue(coords.second in shapeHeight / 2 .. widthAndHeight.second - shapeHeight / 2 )
        }
    }
}