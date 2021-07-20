package apps.smoll.dragdropgame.features.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.ui.formatDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MenuViewModel(val firebaseRepo: FirebaseRepo) : BaseViewModel() {

    private val _lastCompletedLevel: MutableLiveData<LevelStats> = MutableLiveData()
    val lastCompletedLevel: LiveData<LevelStats> get() = _lastCompletedLevel

    private val _connectedToInternet: MutableLiveData<Boolean> = MutableLiveData()
    val connectedToInternet: LiveData<Boolean> get() = _connectedToInternet

    private val _score: MutableLiveData<String> = MutableLiveData()
    val score: LiveData<String> get() = _score

    fun init() = viewModelScope.launch(Dispatchers.IO) {
        val levelStats = firebaseRepo.getLastLevel()
        _lastCompletedLevel.postValue(levelStats)

        levelStats?.let {
            _score.postValue(formatDateTime("mm:ss:SS", it.totalTimeInMillis))
        }

        firebaseRepo.insertFakeHighScores()
    }
}