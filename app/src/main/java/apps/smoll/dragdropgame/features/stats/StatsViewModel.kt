package apps.smoll.dragdropgame.features.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.features.entities.HighScore
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import kotlinx.coroutines.launch


class StatsViewModel(val firebaseRepo : FirebaseRepo) : BaseViewModel() {

    private val _levelStats: MutableLiveData<Set<HighScore>> = MutableLiveData()
    val levelStats: LiveData<Set<HighScore>> get() = _levelStats

    fun init() {
        viewModelScope.launch {
            _levelStats.value = firebaseRepo.getHighscoresByUser()
        }
    }

}