package apps.smoll.dragdropgame.features.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class StartUpViewModelFactory(
    private val firebaseAuth: FirebaseAuth

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartUpViewModel(firebaseAuth) as T
    }
}