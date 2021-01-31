package apps.smoll.dragdropgame.utils

import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.Shape


fun buildShapesWithRandomColorsAndShapeTypes(listOfCoordinates: List<Pair<Int, Int>>): List<Shape> {

    val shuffledShapes = getShuffledShapeArray()
    val shuffledColors = getShuffledColorsArray()

    return listOfCoordinates.mapIndexed { index, shape ->
        Shape(
            shape,
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
    return colorsArray
}