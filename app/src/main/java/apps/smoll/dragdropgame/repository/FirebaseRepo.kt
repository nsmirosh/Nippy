package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.HighScore

interface FirebaseRepo {

    suspend fun getLastLevel(): LevelStats?

    suspend fun addStats(stats: LevelStats): Boolean

    suspend fun getAllLevelStats()
            : List<LevelStats>?


    suspend fun getHighscoresByUser(): Set<HighScore>

    suspend fun setHighScore(highScore: HighScore) : Boolean

    suspend fun getUserHighScore(): HighScore?
}