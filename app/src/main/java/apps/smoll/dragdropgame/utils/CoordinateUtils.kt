package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.*
import java.util.*


const val boundsForShapesWidthAndHeight = 0.8

const val permissibleHitFaultInPixels = 50

fun generateCoordinatesForShapesOnScreen(
    amountToGenerate: Int,
    widthAndHeight: Pair<Int, Int>
): List<Pair<Int, Int>> =
    generateNonCollidingCoordinateList(
        widthAndHeight * boundsForShapesWidthAndHeight, amountToGenerate
    )

fun generateNonCollidingCoordinateList(
    widthHeightBounds: Pair<Int, Int>,
    amountToGenerate: Int
): List<Pair<Int, Int>> {
    val listOfCoords = mutableListOf<Pair<Int, Int>>()
    repeat(amountToGenerate) {
        listOfCoords.add(generateNewShapeCoords(widthHeightBounds, listOfCoords))
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
        shapeCoordinatesList.forEach {
            if (willShapesCollide(randomCoords, it)) {
                shouldContinueLooking = true
                return@forEach
            }
        }
    } while (shouldContinueLooking)
    return randomCoords
}

fun willShapesCollide(
    firstShapeCoords: Pair<Int, Int>,
    secondShapeCoords: Pair<Int, Int>
): Boolean {
    return getDistanceBetween(firstShapeCoords, secondShapeCoords) < shapeSize
}

fun getRandomXYCoordsIn(widthAndHeight: Pair<Int, Int>): Pair<Int, Int> {

    val margin = shapeSize

    val (upperWidthBound, upperHeightBound) = widthAndHeight - margin

    var randomX = Random().nextInt(upperWidthBound)
    if (randomX < margin) {
        randomX = margin
    }

    var randomY = Random().nextInt(upperHeightBound)
    if (randomY < margin) {
        randomY = margin
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


fun areCoordinatesHit(
    dropEventCoords: Pair<Int, Int>,
    shapeOnScreenCoords: Pair<Int, Int>
): Boolean {

    val (shapeXCenter, shapeYCenter) = shapeOnScreenCoords + halfShapeSize

    val permissibleXFaultRange =
        (shapeXCenter - permissibleHitFaultInPixels)..(shapeXCenter + permissibleHitFaultInPixels)
    val permissibleYFaultRange =
        (shapeYCenter - permissibleHitFaultInPixels)..(shapeYCenter + permissibleHitFaultInPixels)

    val isXHit =
        dropEventCoords.first in permissibleXFaultRange
    val isYHit =
        dropEventCoords.second in permissibleYFaultRange

    return isXHit && isYHit
}


operator fun Pair<Int, Int>.minus(toSubstract: Int): Pair<Int, Int> {
    return Pair(this.first - toSubstract, this.second - toSubstract)
}

operator fun Pair<Int, Int>.plus(toAdd: Int): Pair<Int, Int> {
    return Pair(this.first + toAdd, this.second + toAdd)
}

operator fun Pair<Int, Int>.times(toMultiplyBy: Double): Pair<Int, Int> {
    return Pair((this.first * toMultiplyBy).toInt(), (this.second * toMultiplyBy).toInt())
}