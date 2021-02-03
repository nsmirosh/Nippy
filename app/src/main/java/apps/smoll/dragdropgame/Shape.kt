package apps.smoll.dragdropgame

//TODO make sure we're not relying on shape center - this can be confusing. Make this ordinary x, y coordinates
data class Shape(val shapeCenter: Pair<Int, Int>, var typeResource: Int = 0, val colorResource: Int = 0 )

const val shapeSize = 150
const val halfShapeSize = shapeSize / 2
