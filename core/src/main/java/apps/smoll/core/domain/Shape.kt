package apps.smoll.core.domain

data class Shape(val coordinates: Pair<Float, Float>, val shapeType: ShapeType)

enum class ShapeType {
    STAR, SQUARE, CIRCLE, HEXAGON
}