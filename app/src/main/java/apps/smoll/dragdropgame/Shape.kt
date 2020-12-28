package apps.smoll.dragdropgame

data class Shape(val coordinates: Pair<Float, Float>, var shapeType: ShapeType, val shapeColor: Int)

enum class ShapeType {
    STAR, SQUARE, CIRCLE, HEXAGON




}