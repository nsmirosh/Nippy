package apps.smoll.dragdropgame.features.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.utils.firestoreAuth.FirebaseAuthUtils

class GameViewModelFactory(
    private val firebaseRepo: FirebaseRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(firebaseRepo) as T
    }
}