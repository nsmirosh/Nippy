package apps.smoll.dragdropgame.repository

import android.os.Parcelable
import apps.smoll.dragdropgame.features.entities.HighScore
import apps.smoll.dragdropgame.utils.formatDateTime
import apps.smoll.dragdropgame.utils.getCurrentTimeAsDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelStats(
    val dateCompleted: String = getCurrentTimeAsDate(),
    val levelTimeInMillis: Long = 0,
    val totalTimeInMillis: Long = 0,
    val levelToBePlayed: Int = 0,
    var wonCurrentLevel: Boolean = false,
) : Parcelable {

    fun toHighScore() =
        HighScore(
            noOfCompletedLevels = levelToBePlayed.dec(),
            totalTime = totalTimeInMillis,
            dateCompleted = dateCompleted
        )
}


fun LevelStats.isBetterThanCurrentHighScore(currentHighScore: HighScore?): Boolean {
    return currentHighScore == null ||
            (totalTimeInMillis >= currentHighScore.totalTime!! && levelToBePlayed.dec() >= currentHighScore.noOfCompletedLevels!!)
}