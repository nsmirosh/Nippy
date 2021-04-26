package apps.smoll.dragdropgame.features.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.features.game.statsPath
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.Event
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import timber.log.Timber


class StatsViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseRepo : FirebaseRepo = FirebaseRepoImpl()

    private val _levelStats: MutableLiveData<List<LevelStats>> = MutableLiveData()
    val levelStats: LiveData<List<LevelStats>> get() = _levelStats

    fun init() {
        viewModelScope.launch {
            _levelStats.value = firebaseRepo.getAllLevelStats()
        }
    }
}