package apps.smoll.dragdropgame.features.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepo

class GameViewModelFactory(
    private val mApplication: Application,
    private val firebaseRepo: FirebaseRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(mApplication, firebaseRepo) as T
    }
}