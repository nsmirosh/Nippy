package apps.smoll.dragdropgame.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

const val statsPath = "stats"

open class FirebaseRepoImpl(private val firestore: FirebaseFirestore = Firebase.firestore) :
    FirebaseRepo {

    override suspend fun writeLevelStats(stats: LevelStats): Boolean = try {
        firestore
            .collection(statsPath)
            .add(stats)
            .await()
        true
    } catch (e: Exception) {
        Timber.e(e)
        false
    }


    override suspend fun getAllLevelStats()
            : List<LevelStats>? = try {
        firestore
            .collection(statsPath)
            .get()
            .await()
            .map {
                it.toObject(LevelStats::class.java)
            }
    } catch (e: Exception) {
        Timber.e(e)
        null
    }


    override suspend fun getLastLevel(): LevelStats? = try {
        firestore
            .collection(statsPath)
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
}