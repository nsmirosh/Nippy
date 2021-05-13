package apps.smoll.dragdropgame.features.game.inbetween

import androidx.fragment.app.viewModels
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.FragmentInBetweenBinding
import apps.smoll.dragdropgame.features.base.BaseFragment
import apps.smoll.dragdropgame.features.menu.MenuViewModelFactory
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InBetweenFragment : BaseFragment<FragmentInBetweenBinding>(R.layout.fragment_in_between) {

    val viewModel: InBetweenViewModel by viewModels()

    override fun initBindingDependencies() =
        with(binding) {
            viewmodel = viewModel
        }
}