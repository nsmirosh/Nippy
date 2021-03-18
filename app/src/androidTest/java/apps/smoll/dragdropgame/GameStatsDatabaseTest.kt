package apps.smoll.dragdropgame

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import apps.smoll.dragdropgame.database.GameStatsDao
import apps.smoll.dragdropgame.database.GameStatsDatabase
import apps.smoll.dragdropgame.database.LevelStats
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class GameStatsDatabaseTest {

    private lateinit var gameStatsDao: GameStatsDao
    private lateinit var db: GameStatsDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, GameStatsDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        gameStatsDao = db.gameStatsDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetLevelStats() {
        val levelStats = LevelStats()
        levelStats.levelNo = 3
        gameStatsDao.insert(levelStats)
        val actualLevelStats = gameStatsDao.getAllStats()
        Assert.assertEquals(3, actualLevelStats[0].levelNo)
    }

    @Test
    @Throws(Exception::class)
    fun databaseIsCleared() {
        val levelStats = LevelStats()
        gameStatsDao.insert(levelStats)
        val statsInDao = gameStatsDao.getAllStats()
        Assert.assertEquals(1, statsInDao.size)
        gameStatsDao.clear()
        val emptyStatsInDao = gameStatsDao.getAllStats()
        Assert.assertEquals(0, emptyStatsInDao.size)
    }
}