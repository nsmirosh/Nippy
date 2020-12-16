package apps.smoll.dragdropgame

data class Shape(val coordinates: Pair<Float, Float>, var shapeType: ShapeType)

enum class ShapeType {
    STAR, SQUARE, CIRCLE, HEXAGON
}