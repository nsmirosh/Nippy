package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.HighScore

interface FirebaseRepo {

    suspend fun getLastLevel(): LevelStats?

    suspend fun writeLevelStats(stats: LevelStats): Boolean

    suspend fun getAllLevelStats()
            : List<LevelStats>?


    suspend fun getHighscoresByUser(): Set<HighScore>
}