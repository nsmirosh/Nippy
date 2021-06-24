package apps.smoll.dragdropgame.features.entities

data class HighScore(
    var userName: String? = null,
    val noOfCompletedLevels: Int? = null,
    val totalTime: Long? = null,
    val dateCompleted: String? = null
)
