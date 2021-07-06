package apps.smoll.dragdropgame.features.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.FragmentStatsBinding
import apps.smoll.dragdropgame.features.base.BaseFragment
import apps.smoll.dragdropgame.features.entities.domain.HighScore
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.utils.firestoreAuth.FirebaseAuthUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StatsFragment : BaseFragment<FragmentStatsBinding, StatsViewModel>(R.layout.fragment_stats) {

    private val statsViewModel: StatsViewModel by viewModels {
        StatsViewModelFactory(FirebaseRepoImpl(Firebase.firestore, FirebaseAuthUtils(Firebase.auth)))
    }

    override fun initBindingDependencies() {}

    override fun getViewModelInstance() = statsViewModel

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

    private fun setUpRecyclerList(levelStats: List<HighScore>) {
        with(binding.statsRecyclerView) {
            adapter = StatsAdapter(levelStats)
            layoutManager = LinearLayoutManager(activity)
        }
    }
}