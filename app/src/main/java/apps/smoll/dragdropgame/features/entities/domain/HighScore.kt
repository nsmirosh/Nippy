package apps.smoll.dragdropgame.features.entities.domain

import java.util.*

data class HighScore (
    var email: String,
    val noOfCompletedLevels: Int,
    val totalTime: Long,
    val dateCompleted: Date
)