package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.game.statsPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepoImpl : FirebaseRepo {

    private val firestore = Firebase.firestore

    override suspend fun writeLevelStats(stats: LevelStats): Boolean = try {
        firestore
            .collection(statsPath)
            .add(stats)
            .await()
        true
    } catch (e: Exception) {
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
        null
    }


    override suspend fun getLastLevel(): LevelStats? = try {
        val data = firestore
            .collection(statsPath)
            .orderBy("dateCompletedMillis", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        data.documents[0].toObject(LevelStats::class.java)

    } catch (e: Exception) {
        null
    }
}