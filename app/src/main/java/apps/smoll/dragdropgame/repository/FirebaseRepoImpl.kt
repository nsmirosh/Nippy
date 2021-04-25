package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.game.statsPath
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRepoImpl : FirebaseRepo {

    val firestore = Firebase.firestore


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

    override fun getAllLevelStats(): List<LevelStats> {
        val allUserStats = firestore.collection(statsPath).get()
        val stats = mutableListOf<LevelStats>()
        allUserStats.addOnSuccessListener { result ->

            /*      result.map {

                  }*/

            //TODO use this https://betterprogramming.pub/how-to-use-kotlin-coroutines-with-firebase-6f8577a3e00f
            for (document in result) {
                stats.add(document.toObject(LevelStats::class.java))
            }
        }
            .addOnFailureListener { exception ->
                Timber.e("Error getting documents: ${exception.localizedMessage}")
            }

        return stats
    }


    override suspend fun getAllLevelStats1()
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