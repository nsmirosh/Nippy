package apps.smoll.dragdropgame.features.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.repository.FirebaseRepo
import kotlinx.coroutines.launch


class StatsViewModel(val firebaseRepo : FirebaseRepo) : BaseViewModel() {

    private val _levelStats: MutableLiveData<Set<NetworkHighScore>> = MutableLiveData()
    val levelStats: LiveData<Set<NetworkHighScore>> get() = _levelStats

    fun init() {
        viewModelScope.launch {
            _levelStats.value = firebaseRepo.getHighscoresByUser()
        }
    }

}