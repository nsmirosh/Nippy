package apps.smoll.dragdropgame.features.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.database.GameStatsDao
import apps.smoll.dragdropgame.database.GameStatsDatabase
import apps.smoll.dragdropgame.database.LevelStats

class StatsViewModel(application: Application) : AndroidViewModel(application)  {

    val dataSource: GameStatsDao
    val statsList: LiveData<List<LevelStats>>

    init {
        dataSource = GameStatsDatabase.getInstance(application).gameStatsDao
        statsList = dataSource.getAllStats()
    }


}