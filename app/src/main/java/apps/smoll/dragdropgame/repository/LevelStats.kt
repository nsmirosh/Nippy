package apps.smoll.dragdropgame.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelStats(
    val dateCompletedMillis: Long = System.currentTimeMillis(),
    val durationMilli: Long = dateCompletedMillis,
    val totalScore: Int = 0,
    val levelScore: Int = 0,
    val currentLevel: Int = 0,
    val nextLevel: Int= 0,
    var wonCurrentLevel: Boolean = false,
) : Parcelable