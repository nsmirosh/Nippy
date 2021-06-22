package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.HighScore
import apps.smoll.dragdropgame.utils.firestoreAuth.FirebaseAuthUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber

const val statsPath = "stats"
const val users = "users"

open class FirebaseRepoImpl(private val firestore: FirebaseFirestore = Firebase.firestore, private val firestoreAuthUtils: FirebaseAuthUtils) :
    FirebaseRepo {

    override suspend fun writeLevelStats(stats: LevelStats): Boolean = try {
        firestore
            .collection(users)
            .document(firestoreAuthUtils.firebaseAuth.uid!!)
            .collection(statsPath)
            .add(stats)
            .await()

/*            .collection(users)
            .document(firestore.)
//            .document(users)
            .collection(users)
            .document(statsPath)*/



/*
        firestore
            .collection(statsPath)
            .add(stats)
            .await()*/
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


    override suspend fun getHighscoresByUser(): Set<HighScore> {
        return setOf()
    }
}