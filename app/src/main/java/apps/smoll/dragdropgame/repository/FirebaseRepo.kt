package apps.smoll.dragdropgame.repository

interface FirebaseRepo {

    fun writeLevelStats(stats: LevelStats)

    fun getAllLevelStats() : List<LevelStats>?

    suspend fun getAllLevelStats1()
            : List<LevelStats>?
}