package apps.smoll.dragdropgame.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelStats(
    val dateCompletedMillis: Long = System.currentTimeMillis(),
    val durationMilli: Long = dateCompletedMillis,
    val totalScore: Int = 0,
    val levelScore: Int = 0,
    val levelToBePlayed: Int = 0,
    var wonCurrentLevel: Boolean = false,
) : Parcelable