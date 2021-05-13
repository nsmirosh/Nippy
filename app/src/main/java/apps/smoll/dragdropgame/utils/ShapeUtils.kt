package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import apps.smoll.dragdropgame.shapeSize
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

fun copyAndModifyRandomShapeFrom(screenShapes: List<Shape>, width: Int, height: Int): Shape {

    val oneOfTheShapesOnScreen = screenShapes.random()

    val xPos = (width / 2) - shapeSize
    val yPos = (height * 0.7).toInt()

    return oneOfTheShapesOnScreen.copy(
        topLeftCoords = Pair(xPos, yPos),
        colorResource = R.color.shape_to_match_color
    )
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