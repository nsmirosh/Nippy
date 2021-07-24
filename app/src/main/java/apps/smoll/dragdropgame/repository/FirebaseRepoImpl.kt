package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils
import apps.smoll.dragdropgame.utils.firestore.ResultWrapper
import apps.smoll.dragdropgame.utils.firestore.safeApiCall2
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

const val completedLevelsPath = "completedLevels"
const val usersPath = "users"
const val highScorePath = "highscores"

open class FirebaseRepoImpl(
    firestoreUtils: FirebaseUtils,
    private val levelStatsMapper: (QuerySnapshot) -> List<LevelStats>?,
    private val addStatsMapper: (DocumentReference) -> Unit,
    private val highScoreMapper: (DocumentSnapshot) -> HighScore,
    private val highScoreListMapper: (QuerySnapshot) -> List<HighScore>?,
) : FirebaseRepo {

    private val firestore = firestoreUtils.fireStoreInstance

    private val uID = firestoreUtils.authInstance.uid!!
    private val email = firestoreUtils.authInstance.currentUser!!.email!!

    override suspend fun addStats(stats: LevelStats): ResultWrapper<Unit> =
        safeApiCall2(
            addStatsMapper,
            getCurrentUserDocument()
                .collection(completedLevelsPath)
                .add(stats)
        )

    override suspend fun getAllLevelStats(): ResultWrapper<List<LevelStats>?> =
        safeApiCall2(
            levelStatsMapper,
            getCurrentUserDocument()
                .collection(completedLevelsPath)
                .get()
        )

    override suspend fun getUserHighScore(): ResultWrapper<HighScore> =
        safeApiCall2(
            highScoreMapper,
            firestore
                .collection(highScorePath)
                .document(uID)
                .get()
        )

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

    override suspend fun getHighscoresByUserSorted(): ResultWrapper<List<HighScore>?> =
        safeApiCall2(
            highScoreListMapper,
            firestore
                .collection(highScorePath)
                .get()
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

    private fun getCurrentUserDocument(): DocumentReference = firestore
        .collection(usersPath)
        .document(uID)
}