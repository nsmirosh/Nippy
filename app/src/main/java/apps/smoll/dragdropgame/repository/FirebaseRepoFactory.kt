package apps.smoll.dragdropgame.repository

import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.repository.mappers.mapNetworkHighScore
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

object FirebaseRepoFactory {

    fun makeFirebaseRepository(): FirebaseRepo {
        return FirebaseRepoImpl(
            FirebaseUtils(),
            makeLevelStatsMapper(),
            makeAddStatsMapper(),
            makeHighScoreMapper(),
            makeHighScoreListMapper()
        )
    }

    private fun makeLevelStatsMapper(): (QuerySnapshot) -> List<LevelStats>? =
        { snapShot ->
            snapShot.map {
                it.toObject(LevelStats::class.java)
            }
        }

    private fun makeAddStatsMapper(): (DocumentReference) -> Unit =
        { }

    private fun makeHighScoreMapper(): (DocumentSnapshot) -> HighScore = { docReference ->
        val networkHighScore = docReference.toObject(NetworkHighScore::class.java)
        mapNetworkHighScore(networkHighScore)
    }

    private fun makeHighScoreListMapper(): (QuerySnapshot) -> List<HighScore>? = { snapShot ->
        snapShot
            .map {
                it.toObject(NetworkHighScore::class.java)
            }
            .map {
                mapNetworkHighScore(it)
            }
            .sortedDescending()
    }
}