package apps.smoll.dragdropgame.features.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.features.game.MainActivity
import apps.smoll.dragdropgame.utils.extensions.longToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class StartUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up) //TODO add splash screen in theme instead of here
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        var intent = Intent(this, MainActivity::class.java)
        if (auth.currentUser == null) {
            intent = Intent(this, AuthActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}