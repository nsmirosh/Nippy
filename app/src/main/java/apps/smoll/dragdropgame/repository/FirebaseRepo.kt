package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore

interface FirebaseRepo {

    suspend fun getLastLevel(): LevelStats?

    suspend fun addStats(stats: LevelStats): Boolean

    suspend fun getAllLevelStats()
            : List<LevelStats>?


    suspend fun getHighscoresByUser(): Set<NetworkHighScore>

    suspend fun setHighScore(highScore: NetworkHighScore) : Boolean

    suspend fun getUserHighScore(): NetworkHighScore?
    suspend fun insertFakeHighScores(): Boolean
}