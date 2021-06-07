package apps.smoll.dragdropgame.features.startup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.features.auth.AuthActivity
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.features.game.MainActivity
import apps.smoll.dragdropgame.utils.Event
import com.google.firebase.auth.FirebaseAuth

class StartUpViewModel(val firebaseAuth: FirebaseAuth) : BaseViewModel() {

    private val _launchScreenEvent: MutableLiveData<Event<Class<*>>> =
        MutableLiveData()
    val launchScreenEvent: LiveData<Event<Class<*>>> get() = _launchScreenEvent

    fun onStart() {
        if (firebaseAuth.currentUser != null)
            _launchScreenEvent.value = Event(MainActivity::class.java)
        else
            _launchScreenEvent.value = Event(AuthActivity::class.java)
    }
}