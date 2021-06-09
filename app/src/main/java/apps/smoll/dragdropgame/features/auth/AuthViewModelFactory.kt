package apps.smoll.dragdropgame.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.utils.firestoreAuth.FirebaseAuthUtils

class AuthViewModelFactory(
    private val firebaseAuthUtils: FirebaseAuthUtils

) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(firebaseAuthUtils) as T
    }
}