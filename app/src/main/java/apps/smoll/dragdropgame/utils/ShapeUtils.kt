package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import java.util.*

fun buildShapesWithRandomColorsAndShapeTypes(
    level: Int,
    widthAndHeight: Pair<Int, Int>
): List<Shape> {

    val listOfCoordinates = generateCoordinatesForShapesOnScreen(level, widthAndHeight)

    val shuffledShapes = getShuffledShapes()
    val shuffledColors = getShuffledColors()

    return listOfCoordinates.map { coords ->
        Shape(
            coords,
            shuffledShapes.pop(),
            shuffledColors.pop()
        )
    }
}

private fun getShuffledShapes(): Stack<Int> {
    val imageShapeArray = arrayOf(
        R.drawable.ic_square,
        R.drawable.ic_hexagonal,
        R.drawable.ic_star,
        R.drawable.ic_circle
    )
    return Stack<Int>().apply {
        addAll(imageShapeArray)
        shuffle()
    }
}

private fun getShuffledColors(): Stack<Int> {
    val colorsArray = arrayOf(
        R.color.color_1,
        R.color.color_2,
        R.color.color_3,
        R.color.color_4,
    )
    return Stack<Int>().apply {
        addAll(colorsArray)
        shuffle()
    }
}