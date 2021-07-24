package apps.smoll.dragdropgame.features.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.features.entities.network.NetworkHighScore
import apps.smoll.dragdropgame.repository.FirebaseRepo
import apps.smoll.dragdropgame.repository.isBetterThanCurrentHighScore
import apps.smoll.dragdropgame.utils.firestore.ResultWrapper
import kotlinx.coroutines.launch


class StatsViewModel(private val firebaseRepo : FirebaseRepo) : BaseViewModel() {

    private val _levelStats: MutableLiveData<List<HighScore>> = MutableLiveData()
    val levelStats: LiveData<List<HighScore>> get() = _levelStats

    fun init() {
        viewModelScope.launch {
            when (val result = firebaseRepo.getHighscoresByUserSorted()) {
                is ResultWrapper.Success ->  {
                    _levelStats.value = result.value
                }
                is ResultWrapper.GenericError -> {

                }
            }
        }
    }

}