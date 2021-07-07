package apps.smoll.dragdropgame.repository

import android.os.Parcelable
import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.getCurrentTimeAsDate
import apps.smoll.dragdropgame.utils.getDateFrom
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelStats(
    val dateCompleted: String = getCurrentTimeAsDate(),
    val levelTimeInMillis: Long = 0,
    val totalTimeInMillis: Long = 0,
    val levelToBePlayed: Int = 0,
    var wonCurrentLevel: Boolean = false,
) : Parcelable


fun LevelStats.isBetterThanCurrentHighScore(currentHighScore: NetworkHighScore?): Boolean {
    return currentHighScore == null ||
            (totalTimeInMillis >= currentHighScore.totalTime!! && levelToBePlayed.dec() >= currentHighScore.noOfCompletedLevels!!)
}