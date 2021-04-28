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
import apps.smoll.dragdropgame.repository.LevelStats

class StatsFragment : Fragment() {

    private val statsViewModel: StatsViewModel by viewModels()

    lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
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