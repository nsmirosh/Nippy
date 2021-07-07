package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.repository.mappers.HighScoreListMapper
import apps.smoll.dragdropgame.repository.mappers.HighScoreMapper
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
import java.text.SimpleDateFormat
import java.util.*

const val completedLevelsPath = "completedLevels"
const val usersPath = "users"
const val highScorePath = "highscores"

open class FirebaseRepoImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val firestoreAuthUtils: FirebaseAuthUtils
) : FirebaseRepo {

    private val uID = firestoreAuthUtils.firebaseAuth.uid!!
    private val email = firestoreAuthUtils.firebaseAuth.currentUser!!.email!! //TODO refactor this

    private val highScoreListMapper = HighScoreListMapper()
    private val highScoreMapper = HighScoreMapper()


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


    override suspend fun getAllLevelStats(): List<LevelStats>? = try {
        withContext(Dispatchers.IO) {
            getCurrentUserDocument()
                .collection(completedLevelsPath)
                .get()
                .await()
                .map {
                    it.toObject(LevelStats::class.java)
                }
        }
    } catch (e: Exception) {
        Timber.e(e)
        null
    }


    override suspend fun getUserHighScore(): NetworkHighScore? =
        try {
            firestore
                .collection(highScorePath)
                .document(uID)
                .get()
                .await()
                .toObject(NetworkHighScore::class.java)
        } catch (e: Exception) {
            Timber.e(e)
            null
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

    private fun getCurrentUserDocument(): DocumentReference = firestore
        .collection(usersPath)
        .document(uID)

    override suspend fun setHighScore(highScore: HighScore): Boolean = try {
        firestore
            .collection(highScorePath)
            .document(email)
            .set(highScore.copy(email = email))
            .await()
        true
    } catch (e: Exception) {
        Timber.e(e)
        false
    }

    override suspend fun getHighscoresByUserSorted(): List<HighScore> = highScoreListMapper.map(
        firestore
            .collection(highScorePath)
            .get()
            .await()
            .map {
                it.toObject(NetworkHighScore::class.java)
            }
            .sortedDescending()
    )

    override suspend fun insertFakeHighScores() = try {

        val calendar = Calendar.getInstance()

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

        val first = NetworkHighScore("balls@balls.com", 4, 45132, formatter.format(calendar.time))

        calendar.set(2020, 5, 25, 13, 44, 53)

        val second = NetworkHighScore("balls2@ba.com", 10, 105899, formatter.format(calendar.time))

        calendar.set(2021, 4, 23, 17, 55, 14)
        val third = NetworkHighScore("balls3@ba.com", 1, 10444, formatter.format(calendar.time))
        val highscores = mutableListOf(first, second, third)

        highscores.forEach {
            firestore
                .collection(highScorePath)
                .document(it.email!!)
                .set(it)
                .await()
        }
        true
    } catch (e: Exception) {
        Timber.e(e)
        false
    }
}