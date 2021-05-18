package apps.smoll.dragdropgame.features.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.FragmentStatsBinding
import apps.smoll.dragdropgame.features.base.BaseFragment
import apps.smoll.dragdropgame.repository.LevelStats

class StatsFragment : BaseFragment<FragmentStatsBinding>(R.layout.fragment_stats) {

    private val statsViewModel: StatsViewModel by viewModels()


    override fun initBindingDependencies() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startObservingLiveData()
        statsViewModel.init()
    }

    private fun startObservingLiveData() {

        // TODO use this later on https://medium.com/swlh/android-recyclerview-with-data-binding-and-coroutine-3192097a0496
        statsViewModel.levelStats.observe(
            viewLifecycleOwner,
            { setUpRecyclerList(it) }
        )
    }

    private fun setUpRecyclerList(levelStats: List<LevelStats>) {
        with(binding.statsRecyclerView) {
            adapter = StatsAdapter(levelStats)
            layoutManager = LinearLayoutManager(activity)
        }
    }
}