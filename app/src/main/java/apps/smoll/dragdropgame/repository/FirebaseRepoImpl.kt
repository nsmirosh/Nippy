package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.game.statsPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepoImpl : FirebaseRepo {

    private val firestore = Firebase.firestore

    override fun writeLevelStats(stats: LevelStats) {
        firestore.collection(statsPath)
            .add(stats)
            .addOnSuccessListener { documentReference ->
                Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Timber.e("Error adding document = ${e.localizedMessage}")
            }
    }


    override suspend fun writeLevelStats1(stats: LevelStats): Boolean = try {
        val data = firestore
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


}