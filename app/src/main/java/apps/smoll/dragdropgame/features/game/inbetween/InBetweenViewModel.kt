package apps.smoll.dragdropgame.features.game.inbetween

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.smoll.dragdropgame.repository.LevelStats

class InBetweenViewModel : ViewModel() {

    private val _levelStats: MutableLiveData<LevelStats> = MutableLiveData()
    val levelStats: LiveData<LevelStats> get() = _levelStats


    fun initWithArgs(stats: LevelStats) {
        _levelStats.value = stats
    }
}