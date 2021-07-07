package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore

interface FirebaseRepo {

    suspend fun getLastLevel(): LevelStats?

    suspend fun addStats(stats: LevelStats): Boolean

    suspend fun getAllLevelStats(): List<LevelStats>?

    suspend fun getHighscoresByUserSorted(): List<HighScore>

    suspend fun setHighScore(highScore: HighScore) : Boolean

    suspend fun getUserHighScore(): NetworkHighScore?

    suspend fun insertFakeHighScores(): Boolean
}