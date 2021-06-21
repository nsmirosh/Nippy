package apps.smoll.dragdropgame.features.entities

data class HighScore(
    val userName: String,
    val noOfCompletedLevels: Int,
    val totalTime: String,
    val dateCompleted: String
)
