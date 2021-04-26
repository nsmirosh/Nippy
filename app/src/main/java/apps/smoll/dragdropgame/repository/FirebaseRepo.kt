package apps.smoll.dragdropgame.repository

interface FirebaseRepo {

    fun writeLevelStats(stats: LevelStats)

    suspend fun writeLevelStats1(stats: LevelStats): Boolean

    suspend fun getAllLevelStats()
            : List<LevelStats>?
}