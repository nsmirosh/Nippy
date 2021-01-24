package apps.smoll.dragdropgame

import apps.smoll.dragdropgame.utils.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random


val screenDensities =
    setOf(Pair(480, 800), Pair(1720, 1280), Pair(1440, 2560), Pair(1080, 1920))

class CoordinateUtilsTest {

    @Test
    fun getRandomXYCoordinates_withValidParameters_isWithinProperRange() {

        screenDensities.forEach { widthHeight ->
            val coords = getRandomXYCoordsIn(widthHeight)
            val xRange = halfShapeSize..widthHeight.first - halfShapeSize
            assertTrue(coords.first in xRange)
            val yRange = halfShapeSize..widthHeight.second - halfShapeSize
            assertTrue(coords.second in yRange)
        }
    }

    @Test
    fun getDistanceBetweenTwoPoints_withValidParams_returnValidDistance() {
        val distance = getDistanceBetween(Pair(100, 100), Pair(200, 200))
        assertThat(141, `is`(distance.toInt()))
    }

    @Test
    fun generateNewShapeCoords_withAlreadyPresentShapes_returnsValidCoordinatesForNewShape() {

        val amountOfShapesGeneratedBeforeHand = 5

        screenDensities.forEach { widthHeight ->

            val centersOfShapes = mutableSetOf<Pair<Int, Int>>()
            repeat(amountOfShapesGeneratedBeforeHand) {
                var randomX = Random.nextInt(widthHeight.first - halfShapeSize)
                if (randomX < halfShapeSize) {
                    randomX = halfShapeSize
                }
                var randomY = Random.nextInt(widthHeight.second - halfShapeSize)
                if (randomY < halfShapeSize) {
                    randomY = halfShapeSize
                }
                centersOfShapes.add(Pair(randomX, randomY))
            }

            val shapeList = mutableListOf<Pair<Int, Int>>()

            centersOfShapes.forEach {
                shapeList.add(it)
            }

            val newShapeCoords = generateNewShapeCoords(widthHeight, shapeList)
            for (shapeCenter in centersOfShapes) {
                assertThat(
                    getDistanceBetween(shapeCenter, newShapeCoords).toInt(),
                    greaterThan(shapeSize)
                )
            }
        }
    }

    @Test
    fun generateNonCollidingCoordinateList_withValidParams_returnsNonCollidingCoordinates() {
        val screenWidth = 1000
        val screenHeight = 1200
        val generatedCoordinates =
            generateNonCollidingCoordinateList(Pair(screenWidth, screenHeight), 5).toMutableList()

        for (coordinates in generatedCoordinates) {
            generatedCoordinates.toMutableList().apply {
                remove(coordinates)
                for (coordsToCompareWith in this) {
                    assertThat(
                        getDistanceBetween(coordsToCompareWith, coordinates).toInt(),
                        greaterThan(shapeSize)
                    )
                }
            }
        }
    }
}