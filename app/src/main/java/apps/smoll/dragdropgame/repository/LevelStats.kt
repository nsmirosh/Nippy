package apps.smoll.dragdropgame.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelStats(
    val dateCompletedMillis: Long = System.currentTimeMillis(),
    val durationMilli: Long = dateCompletedMillis,
    val totalScore: Int = -1,
    val levelScore: Int = -1,
    var level: Int = -1
) : Parcelable