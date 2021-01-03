package apps.smoll.dragdropgame

data class Shape(val shapeCenter: Pair<Int, Int>, var typeResource: Int = 0, val colorResource: Int = 0)

enum class ShapeType {
    STAR, SQUARE, CIRCLE, HEXAGON
}

const val shapeHeight = 150
const val shapeWidth = 150
const val halfShapeWidth = shapeWidth / 2
const val halfShapeHeight = shapeHeight / 2