package apps.smoll.dragdropgame.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level_stats_table")
data class LevelStats(
    @PrimaryKey(autoGenerate = true)
    var levelLogId: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "duration_milli")
    val durationMilli: Long = startTimeMilli,

    @ColumnInfo(name = "amount_of_shapes_matched")
    var amountOfShapesMatched: Int = -1)