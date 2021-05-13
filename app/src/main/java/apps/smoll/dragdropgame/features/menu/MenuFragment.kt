package apps.smoll.dragdropgame.features.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.FragmentMenuBinding
import apps.smoll.dragdropgame.features.game.GameViewModelFactory
import apps.smoll.dragdropgame.repository.FirebaseRepoImpl
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuFragment : Fragment() {

    val viewModel: MenuViewModel by viewModels {
        MenuViewModelFactory(FirebaseRepoImpl(Firebase.firestore))
    }

    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        with(binding) {
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
        with (binding) {
            startNewGameButton.setOnClickListener {
                view.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToGameFragment())
            }

            resumeButton.setOnClickListener {
                view.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToGameFragment(viewModel.lastCompletedLevel.value))
            }

            statsButton.setOnClickListener {
                view.findNavController().navigate(MenuFragmentDirections.actionGameFragmentToStatsFragment())
            }
        }
    }
}