package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.*
import java.util.*


fun generateNonCollidingCoordinateList(widthAndHeight: Pair<Int, Int>, neededAmount: Int): List<Pair<Int, Int>> {
    val listOfCoords = mutableListOf<Pair<Int, Int>>()
    repeat(neededAmount) {
        listOfCoords.add(generateNewShapeCoords(widthAndHeight, listOfCoords))
    }
    return listOfCoords
}


fun generateNewShapeCoords(
    widthAndHeight: Pair<Int, Int>,
    shapeCoordinatesList: List<Pair<Int, Int>>
): Pair<Int, Int> {

    var randomCoords: Pair<Int, Int>
    do {
        var shouldContinueLooking = false
        randomCoords = getRandomXYCoordsIn(widthAndHeight)
        for (shapeCoordinates in shapeCoordinatesList) {
            if (willShapesCollide(randomCoords, shapeCoordinates)) {
                shouldContinueLooking = true
                break
            }
        }
    } while (shouldContinueLooking)
    return randomCoords
}


fun willShapesCollide(
    firstShapeCoords: Pair<Int, Int>,
    secondShapeCoords: Pair<Int, Int>
): Boolean {
    return getDistanceBetween(firstShapeCoords, secondShapeCoords) < shapeWidth
}

fun getRandomXYCoordsIn(widthAndHeight: Pair<Int, Int>): Pair<Int, Int> {

    val upperWidthBound = widthAndHeight.first - halfShapeWidth

    var randomX = Random().nextInt(upperWidthBound)
    if (randomX < halfShapeWidth) {
        randomX = halfShapeWidth
    }

    val upperHeightBound = widthAndHeight.second - halfShapeHeight

    var randomY = Random().nextInt(upperHeightBound)
    if (randomY < halfShapeHeight) {
        randomY = halfShapeHeight
    }
    return Pair(randomX, randomY)
}

fun getDistanceBetween(
    firstPoint: Pair<Int, Int>,
    secondPoint: Pair<Int, Int>
): Double {

    return Math.sqrt(
        Math.pow(
            (firstPoint.first - secondPoint.first).toDouble(),
            2.0
        ) + Math.pow((firstPoint.second - secondPoint.second).toDouble(), 2.0)
    )
}