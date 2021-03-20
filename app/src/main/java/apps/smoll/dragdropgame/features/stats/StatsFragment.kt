package apps.smoll.dragdropgame.features.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.database.LevelStats
import apps.smoll.dragdropgame.features.game.GameViewModel
import apps.smoll.dragdropgame.features.menu.MenuFragmentDirections
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : Fragment(R.layout.fragment_stats) {

    private val statsViewModel: StatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObservingLiveData()
    }

    private fun startObservingLiveData() {
        statsViewModel.statsList.observe(
            viewLifecycleOwner,
            { setUpRecyclerList(it) }
        )
    }

    private fun setUpRecyclerList(levelStats: List<LevelStats>) {
        with(statsRecyclerView) {
            adapter = StatsAdapter(levelStats)
            layoutManager = LinearLayoutManager(activity)
        }
    }
}