package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.features.game.MainFragment
import java.util.*

fun determineCoordinatesForNewShape(
    widthAndHeight: Pair<Int, Int>,
    shapes: List<Shape>
): Pair<Int, Int> {

    var randomCoords: Pair<Int, Int>
    do {
        randomCoords = getRandomScreenCoords(widthAndHeight)
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

private fun getRandomScreenCoords(widthAndHeight: Pair<Int, Int>): Pair<Int, Int> {

    val lowerHeightBound = MainFragment.shapeHeight / 2
    val upperHeightBound = widthAndHeight.first - MainFragment.shapeHeight / 2

    var randomY = 0
    do {
        randomY = Random().nextInt(upperHeightBound)
    } while (randomY < lowerHeightBound)

    val lowerWidthBound = MainFragment.shapeWidth / 2
    val upperWidthBound = widthAndHeight.second - MainFragment.shapeWidth / 2

    var randomX = 0
    do {
        randomX = Random().nextInt(upperWidthBound)
    } while (randomX < lowerWidthBound)

    return Pair(randomX, randomY)
}

private fun getDistanceBetweenTwoPoints(
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