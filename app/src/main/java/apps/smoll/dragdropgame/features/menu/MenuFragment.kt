package apps.smoll.dragdropgame.features.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.FragmentMenuBinding
import apps.smoll.dragdropgame.features.base.BaseFragment
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import apps.smoll.dragdropgame.utils.extensions.longToast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class MenuFragment : BaseFragment<FragmentMenuBinding, MenuViewModel>(R.layout.fragment_menu) {

    val menuViewModel: MenuViewModel by viewModels {
        MenuViewModelFactory(FirebaseRepoImpl(Firebase.firestore))
    }

    override fun initBindingDependencies() {
        binding.viewmodel = menuViewModel
    }

    override fun getViewModelInstance() = menuViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuViewModel.init()
        with(binding) {
            startNewGameButton.setOnClickListener {
                view.findNavController()
                    .navigate(MenuFragmentDirections.actionMenuFragmentToGameFragment())
            }

            resumeButton.setOnClickListener {
                view.findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToGameFragment(
                        menuViewModel.lastCompletedLevel.value
                    )
                )
            }

            statsButton.setOnClickListener {
                view.findNavController()
                    .navigate(MenuFragmentDirections.actionGameFragmentToStatsFragment())
            }
        }
    }
}