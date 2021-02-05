package apps.smoll.dragdropgame

data class Shape(val topLeftCoords: Pair<Int, Int>, val typeResource: Int = 0, val colorResource: Int = 0 )

const val shapeSize = 150
const val halfShapeSize = shapeSize / 2
