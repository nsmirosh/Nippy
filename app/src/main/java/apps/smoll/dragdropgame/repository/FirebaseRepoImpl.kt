package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.HighScore
import apps.smoll.dragdropgame.utils.firestoreAuth.FirebaseAuthUtils
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

const val completedLevelsPath = "completedLevels"
const val usersPath = "users"
const val highScorePath = "highscores"

open class FirebaseRepoImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    firestoreAuthUtils: FirebaseAuthUtils
) : FirebaseRepo {

    private val uID = firestoreAuthUtils.firebaseAuth.uid!!


    override suspend fun addStats(stats: LevelStats): Boolean = try {
        getCurrentUserDocument()
            .collection(completedLevelsPath)
            .add(stats)
            .await()
        true
    } catch (e: Exception) {
        Timber.e(e)
        false
    }


    override suspend fun getAllLevelStats() = withContext(Dispatchers.IO) {
        try {
            getCurrentUserDocument()
                .collection(completedLevelsPath)
                .get()
                .await()
                .map {
                    it.toObject(LevelStats::class.java)
                }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    override suspend fun getUserHighScore() =
        getAllLevelStats()
            ?.maxByOrNull { it.levelToBePlayed }
            ?.let {
                HighScore(
                    "balls",
                    it.levelToBePlayed.dec(),
                    it.totalTimeInMillis,
                    it.dateCompleted
                )
            }


    override suspend fun getLastLevel(): LevelStats? = try {
        getCurrentUserDocument()
            .collection(completedLevelsPath)
            .orderBy("dateCompleted", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .documents
            .first()
            .toObject(LevelStats::class.java)

    } catch (e: Exception) {
        Timber.e(e)
        null
    }


    private fun getCurrentUserDocument() = firestore
        .collection(usersPath)
        .document(uID)

    override suspend fun setHighScore(highScore: HighScore): Boolean = try {
        firestore
            .collection(highScorePath)
            .document(uID)
            .set(highScore)
            .await()
        true
    } catch (e: Exception) {
        Timber.e(e)
        false
    }

    override suspend fun getHighscoresByUser(): Set<HighScore> {


        return setOf()
    }
}