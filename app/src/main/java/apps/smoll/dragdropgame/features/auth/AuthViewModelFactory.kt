package apps.smoll.dragdropgame.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils

class AuthViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(FirebaseUtils()) as T
    }
}