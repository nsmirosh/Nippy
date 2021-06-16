package apps.smoll.dragdropgame.features.game.inbetween

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.repository.LevelStats
import apps.smoll.dragdropgame.utils.formatDateTime

class InBetweenViewModel : BaseViewModel() {

    private val _levelStats: MutableLiveData<LevelStats> = MutableLiveData()
    val levelStats: LiveData<LevelStats> get() = _levelStats

    private val _score: MutableLiveData<String> = MutableLiveData()
    val score: LiveData<String> get() = _score

    fun initWithArgs(stats: LevelStats) {
        _levelStats.value = stats
        _score.value = formatDateTime("mm:ss:SS", stats.totalTimeInMillis)
    }
}