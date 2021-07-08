package apps.smoll.dragdropgame.features.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils

class StatsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatsViewModel(
            FirebaseRepoImpl(
                FirebaseUtils()
            )
        ) as T
    }
}