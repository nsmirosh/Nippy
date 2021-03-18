package apps.smoll.dragdropgame.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level_stats_table")
data class LevelStats(
    @PrimaryKey(autoGenerate = true)
    var levelLogId: Long = 0L,

    @ColumnInfo(name = "date_completed_millis")
    val dateCompletedMillis: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "duration_milli")
    val durationMilli: Long = dateCompletedMillis,

    @ColumnInfo(name = "level_no")
    var levelNo: Int = -1)