package apps.smoll.dragdropgame

data class Shape(val coordinates: Pair<Int, Int>, var typeResource: Int, val colorResource: Int)

enum class ShapeType {
    STAR, SQUARE, CIRCLE, HEXAGON
}

const val shapeHeight = 150
const val shapeWidth = 150