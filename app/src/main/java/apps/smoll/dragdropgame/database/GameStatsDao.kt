package apps.smoll.dragdropgame.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameStatsDao {

    @Insert
    fun insert(levelStats: LevelStats)

    @Update
    fun update(levelStats: LevelStats)


    @Query("SELECT * FROM level_stats_table")
    fun getAllStats() : List<LevelStats>


    @Query("DELETE FROM level_stats_table")
    fun clear()

}