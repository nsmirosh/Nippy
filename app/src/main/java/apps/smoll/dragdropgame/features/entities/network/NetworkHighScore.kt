package apps.smoll.dragdropgame.features.entities.network

data class NetworkHighScore(
    var email: String? = null,
    val noOfCompletedLevels: Int? = null,
    val totalTime: Long? = null,
    val dateCompleted: String? = null
)
