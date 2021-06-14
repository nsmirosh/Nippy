package apps.smoll.dragdropgame.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelStats(
    val dateCompletedMillis: Long = System.currentTimeMillis(),
    val levelTimeInMillis: Long = 0,
    val totalTimeInMillis: Long = 0,
    val levelToBePlayed: Int = 0,
    var wonCurrentLevel: Boolean = false,
) : Parcelable