package apps.smoll.dragdropgame.features.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import kotlinx.coroutines.launch


class MenuViewModel : ViewModel() {

    val firebaseRepo : FirebaseRepo = FirebaseRepoImpl()
    private val _lastCompletedLevel: MutableLiveData<LevelStats> = MutableLiveData()
    val lastCompletedLevel: LiveData<LevelStats> get() = _lastCompletedLevel

    fun init() {
        viewModelScope.launch {
            _lastCompletedLevel.value = firebaseRepo.getLastLevel()
        }
    }
}