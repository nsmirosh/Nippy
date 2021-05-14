package apps.smoll.dragdropgame.features.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.repository.LevelStats
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MenuViewModel(val firebaseRepo : FirebaseRepo) : ViewModel() {

    private val _lastCompletedLevel: MutableLiveData<LevelStats> = MutableLiveData()
    val lastCompletedLevel: LiveData<LevelStats> get() = _lastCompletedLevel

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            _lastCompletedLevel.postValue(firebaseRepo.getLastLevel())
        }
    }
}