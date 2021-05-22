package apps.smoll.dragdropgame.features.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.utils.extensions.longToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class StartUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up) //TODO add splash screen in theme instead of here
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}