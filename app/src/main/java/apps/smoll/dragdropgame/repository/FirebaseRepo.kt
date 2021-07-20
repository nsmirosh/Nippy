package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.firestore.ResultWrapper
import com.google.firebase.firestore.DocumentReference

interface FirebaseRepo {

    suspend fun getLastLevel(): LevelStats?

    suspend fun addStats(stats: LevelStats): ResultWrapper<Unit>

    suspend fun getAllLevelStats(): ResultWrapper<List<LevelStats>?>

    suspend fun getHighscoresByUserSorted(): List<HighScore>?

    suspend fun setHighScore(highScore: HighScore) : Boolean

    suspend fun getUserHighScore(): NetworkHighScore?

    suspend fun insertFakeHighScores(): Boolean
}