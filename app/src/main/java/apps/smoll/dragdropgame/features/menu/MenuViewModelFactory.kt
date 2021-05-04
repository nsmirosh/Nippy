package apps.smoll.dragdropgame.features.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepo

class MenuViewModelFactory(
    private val firebaseRepo: FirebaseRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(firebaseRepo) as T
    }
}