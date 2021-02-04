package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape
import java.util.*


fun buildShapesWithRandomColorsAndShapeTypes(level: Int, widthAndHeight: Pair<Int, Int>): List<Shape> {

    val listOfCoordinates = generateCoordinatesForShapesOnScreen(level, widthAndHeight)

    val shuffledShapes = getShuffledShapeArray().toMutableList()
    val shuffledColors = getShuffledColorsArray()


   /* return listOfCoordinates.map {
        Shape(

            it,
            shuffledShapes
        )

    }*/

    return listOfCoordinates.mapIndexed { index, coords ->
        Shape(
            coords,
            shuffledShapes[index],
            shuffledColors[index]
        )
    }
}

private fun getShuffledShapeArray(): Array<Int> {
    val imageShapeArray = arrayOf(
        R.drawable.ic_square,
        R.drawable.ic_hexagonal,
        R.drawable.ic_star,
        R.drawable.ic_circle
    )
    imageShapeArray.shuffle()
    return imageShapeArray
}


private fun getShuffledColorsArray(): Array<Int> {
    val colorsArray = arrayOf(
        R.color.color_1,
        R.color.color_2,
        R.color.color_3,
        R.color.color_4,
    )

    colorsArray.shuffle()

    val stack = Stack<Int>()
    
    stack.addAll(colorsArray)
    return colorsArray
}