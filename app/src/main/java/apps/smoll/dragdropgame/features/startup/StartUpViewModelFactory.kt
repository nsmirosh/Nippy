package apps.smoll.dragdropgame.features.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.utils.firestoreAuth.FirebaseAuthUtils

class StartUpViewModelFactory(
    private val firebaseAuthUtils: FirebaseAuthUtils

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartUpViewModel(firebaseAuthUtils) as T
    }
}