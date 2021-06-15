package apps.smoll.dragdropgame.features.game.inbetween

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.FragmentInBetweenBinding
import apps.smoll.dragdropgame.features.base.BaseFragment
import apps.smoll.dragdropgame.features.game.GameFragmentArgs
import apps.smoll.dragdropgame.features.menu.MenuFragmentDirections
import apps.smoll.dragdropgame.features.menu.MenuViewModelFactory
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InBetweenFragment : BaseFragment<FragmentInBetweenBinding, InBetweenViewModel>(R.layout.fragment_in_between) {

    val viewModel: InBetweenViewModel by viewModels()

    val args: InBetweenFragmentArgs by navArgs()

    override fun initBindingDependencies() {
        binding.viewmodel = viewModel
    }

    override fun getViewModelInstance() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initWithArgs(args.levelStats)
        with(binding) {
            val goToGameFragment = { v: View ->
                view.findNavController()
                    .navigate(InBetweenFragmentDirections.actionInBetweenFragmentToGameFragment(args.levelStats))
            }
            retryButton.setOnClickListener(goToGameFragment)
            nextLevelButton.setOnClickListener(goToGameFragment)
            mainMenuButton.setOnClickListener {
                view.findNavController()
                    .navigate(InBetweenFragmentDirections.actionInBetweenFragmentToMenuFragment())
            }
        }
    }
}