package apps.smoll.dragdropgame.features.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepo

class StatsViewModelFactory(
    private val firebaseRepo: FirebaseRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatsViewModel(firebaseRepo) as T
    }
}