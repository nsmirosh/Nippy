package apps.smoll.dragdropgame

data class Shape(val shapeCenter: Pair<Int, Int>, var typeResource: Int = 0, val colorResource: Int = 0, val shapeSize: Int = 150 )

const val shapeSize = 150
const val halfShapeSize = shapeSize / 2
