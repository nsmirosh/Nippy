package apps.smoll.dragdropgame.features.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils

class MenuViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(FirebaseRepoImpl(FirebaseUtils())) as T
    }
}