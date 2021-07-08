package apps.smoll.dragdropgame.features.startup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class StartUpActivity : AppCompatActivity() {

    private val startUpViewModel: StartUpViewModel by viewModels {
        StartUpViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up) //TODO add splash screen in theme instead of here


        startUpViewModel.launchScreenEvent.observe(
            this,
            {
                it.getContentIfNotHandled()?.let { screen ->
                    handleGoToScreen(screen)
                }
            }
        )
    }

    private fun handleGoToScreen(classToGoTo: Class<*>) = Intent(this, classToGoTo).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

    override fun onStart() {
        super.onStart()
        startUpViewModel.onStart()
    }
}