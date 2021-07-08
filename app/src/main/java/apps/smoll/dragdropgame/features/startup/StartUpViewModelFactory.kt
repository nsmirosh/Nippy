package apps.smoll.dragdropgame.features.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartUpViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StartUpViewModel(FirebaseUtils()) as T
    }
}