package apps.smoll.dragdropgame.features.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.ActivityAuthBinding
import apps.smoll.dragdropgame.features.game.MainActivity
import apps.smoll.dragdropgame.utils.extensions.snackBar
import apps.smoll.dragdropgame.utils.firestore.FirebaseUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class AuthActivity : AppCompatActivity() {

    private lateinit var gClient: GoogleSignInClient
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build().let { gClient = GoogleSignIn.getClient(this, it) }

        initViews()
    }

    private fun initViews() =
        binding.signInButton.setOnClickListener {
            resultLauncher.launch(gClient.signInIntent)
        }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Timber.e("Google sign in failed with message: ${e.message}")
                }
            } else {
                Timber.d("result =${result}")
            }
        }


    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    window.decorView.snackBar("signInWithCredential:failure ${task.exception}")
                }
            }
    }
}