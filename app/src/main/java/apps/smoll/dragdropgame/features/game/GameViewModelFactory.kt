package apps.smoll.dragdropgame.features.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils

class GameViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(FirebaseRepoImpl(FirebaseUtils())) as T
    }
}