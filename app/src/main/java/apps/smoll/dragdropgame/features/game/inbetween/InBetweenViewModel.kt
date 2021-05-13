package apps.smoll.dragdropgame.features.game.inbetween

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.repository.LevelStats

class InBetweenViewModel: ViewModel() {

    private val _levelStats: MutableLiveData<LevelStats> = MutableLiveData()
    val levelStats: LiveData<LevelStats> get() = _levelStats


}