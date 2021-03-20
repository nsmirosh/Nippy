package apps.smoll.dragdropgame.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameStatsDao {

    @Insert
    suspend fun insert(levelStats: LevelStats)

    @Update
    suspend fun update(levelStats: LevelStats)

    @Query("SELECT * FROM level_stats_table")
    fun getAllStats() : LiveData<List<LevelStats>>

    @Query("DELETE FROM level_stats_table")
    suspend fun clear()
}