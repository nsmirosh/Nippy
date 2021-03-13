package apps.smoll.dragdropgame.features.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import apps.smoll.dragdropgame.R
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment(R.layout.fragment_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startNewGameButton.setOnClickListener {
            view.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToGameFragment())
        }
    }
}