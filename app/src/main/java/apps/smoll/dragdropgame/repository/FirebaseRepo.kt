package apps.smoll.dragdropgame.repository

interface FirebaseRepo {

    suspend fun getLastLevel(): LevelStats?

    suspend fun writeLevelStats(stats: LevelStats): Boolean

    suspend fun getAllLevelStats()
            : List<LevelStats>?
}