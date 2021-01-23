package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.*
import apps.smoll.dragdropgame.features.game.screenHeightRatio
import apps.smoll.dragdropgame.features.game.screenWidthRatio
import java.util.*


fun generateNonCollidingCoordinateList(
    widthAndHeight: Pair<Int, Int>,
    amountToGenerate: Int
): List<Pair<Int, Int>> {
    val listOfCoords = mutableListOf<Pair<Int, Int>>()
    repeat(amountToGenerate) {
        listOfCoords.add(generateNewShapeCoords(widthAndHeight, listOfCoords))
    }
    return listOfCoords
}

fun getShapeSize(screenWidthAndHeight: Pair<Int, Int>) = with(screenWidthAndHeight) {
    if (this.first < this.second) this.first / screenWidthRatio else this.second / screenHeightRatio
}

fun generateNewShapeCoords(
    widthAndHeight: Pair<Int, Int>,
    shapeCoordinatesList: List<Pair<Int, Int>>
): Pair<Int, Int> {
    val shapeSize = getShapeSize(widthAndHeight)
    var randomCoords: Pair<Int, Int>
    do {
        var shouldContinueLooking = false
        randomCoords = getRandomXYCoordsIn(widthAndHeight, shapeSize)
        shapeCoordinatesList.forEach {
            if (willShapesCollide(randomCoords, it, shapeSize)) {
                shouldContinueLooking = true
                return@forEach
            }
        }
    } while (shouldContinueLooking)
    return randomCoords
}

fun willShapesCollide(
    firstShapeCoords: Pair<Int, Int>,
    secondShapeCoords: Pair<Int, Int>,
    shapeSize: Int
): Boolean {
    return getDistanceBetween(firstShapeCoords, secondShapeCoords) < shapeSize
}

fun getRandomXYCoordsIn(widthAndHeight: Pair<Int, Int>, shapeSize: Int): Pair<Int, Int> {

    val upperWidthBound = widthAndHeight.first - shapeSize

    var randomX = Random().nextInt(upperWidthBound)
    if (randomX < shapeSize) {
        randomX = shapeSize
    }

    val upperHeightBound = widthAndHeight.second - shapeSize

    var randomY = Random().nextInt(upperHeightBound)
    if (randomY < shapeSize) {
        randomY = shapeSize
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