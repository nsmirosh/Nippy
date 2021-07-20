package apps.smoll.dragdropgame.features.startup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apps.smoll.dragdropgame.features.auth.AuthActivity
import apps.smoll.dragdropgame.features.base.BaseViewModel
import apps.smoll.dragdropgame.features.game.MainActivity
import apps.smoll.dragdropgame.utils.events.Event
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils

class StartUpViewModel(val firebaseUtils: FirebaseUtils) : BaseViewModel() {

    private val _launchScreenEvent: MutableLiveData<Event<Class<*>>> =
        MutableLiveData()
    val launchScreenEvent: LiveData<Event<Class<*>>> get() = _launchScreenEvent

    fun onStart() {
        if (firebaseUtils.isAuthenticated())
            _launchScreenEvent.value = Event(MainActivity::class.java)
        else
            _launchScreenEvent.value = Event(AuthActivity::class.java)
    }
}