package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.shapeHeight
import apps.smoll.dragdropgame.shapeWidth
import java.util.*

fun determineCoordinatesForNewShape(
    widthAndHeight: Pair<Int, Int>,
    shapes: List<Shape>
): Pair<Int, Int> {

    var randomCoords: Pair<Int, Int>
    do {
        randomCoords = getRandomXYCoords(widthAndHeight)
        var fittingCoordinatesFound = false
        for (shape in shapes) {
            if (getDistanceBetweenTwoPoints(randomCoords, shape.coordinates) > 150) {
                fittingCoordinatesFound = true
                break
            }
        }
    } while (!fittingCoordinatesFound)
    return randomCoords
}

fun getRandomXYCoords(widthAndHeight: Pair<Int, Int>): Pair<Int, Int> {


    val lowerWidthBound = shapeWidth / 2
    val upperWidthBound = widthAndHeight.second - shapeWidth / 2

    var randomX = 0
    do {
        randomX = Random().nextInt(upperWidthBound)
    } while (randomX < lowerWidthBound)

    val lowerHeightBound = shapeHeight / 2
    val upperHeightBound = widthAndHeight.first - shapeHeight / 2

    var randomY = 0
    do {
        randomY = Random().nextInt(upperHeightBound)
    } while (randomY < lowerHeightBound)


    return Pair(randomX, randomY)
}

fun getDistanceBetweenTwoPoints(
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