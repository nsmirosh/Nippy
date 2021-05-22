package apps.smoll.dragdropgame.features.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import apps.smoll.dragdropgame.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}