package apps.smoll.dragdropgame

import apps.smoll.dragdropgame.utils.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random


class CoordinateUtilsTest {

    @Test
    fun getRandomXYCoordinates_withValidParameters_isWithinProperRange() {
        for (value in 200..2000 step 50) {
            val coords = getRandomXYCoordsIn(Pair(value, value))
            val xRange = halfShapeWidth..value - halfShapeWidth
            assertTrue(coords.first in xRange)
            val yRange = halfShapeHeight..value - halfShapeHeight
            assertTrue(coords.second in yRange)
        }
    }

    @Test
    fun getDistanceBetweenTwoPoints_withValidParams_returnValidDistance() {
        val distance = getDistanceBetween(Pair(100, 100), Pair(200, 200))
        assertEquals(141, `is`(distance.toInt()))
    }

    @Test
    fun generateNewShapeCoords_withValidParams_returnsValidCoordinates() {
        val centersOfShapes = mutableSetOf<Pair<Int, Int>>()
        val screenWidth = 1000
        val screenHeight = 1200

        repeat(5) {
            var randomX = Random.nextInt(screenWidth - halfShapeWidth)
            if (randomX < halfShapeWidth) {
                randomX = halfShapeWidth
            }
            var randomY = Random.nextInt(screenHeight - halfShapeHeight)
            if (randomY < halfShapeHeight) {
                randomY = halfShapeHeight
            }
            centersOfShapes.add(Pair(randomX, randomY))
        }

        val shapeList = mutableListOf<Pair<Int, Int>>()

        shapeList.apply {
            for (coordinates in centersOfShapes) {
                shapeList.add(coordinates)
            }
        }

        val newShapeCoords = generateNewShapeCoords(Pair(screenWidth, screenHeight), shapeList)
        for (shapeCenter in centersOfShapes) {
            assertTrue(getDistanceBetween(shapeCenter, newShapeCoords) > shapeWidth)
        }
    }

    @Test
    fun generateNonCollidingCoordinateList_withValidParams_returnsValidCoordinates() {
        val screenWidth = 1000
        val screenHeight = 1200
        val generatedCoordinates = generateNonCollidingCoordinateList(Pair(screenWidth, screenHeight), 5)

        for (coordinates in generatedCoordinates) {
            generatedCoordinates.toMutableList().apply {
                remove(coordinates)
                for (coordsToCompareWith in this) {
                    assertTrue(getDistanceBetween(coordsToCompareWith, coordinates) > shapeWidth)
                }
            }

        }
    }
}