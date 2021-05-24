package apps.smoll.dragdropgame.features.auth

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import apps.smoll.dragdropgame.R
import apps.smoll.dragdropgame.databinding.ActivityAuthBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        auth = Firebase.auth
        val view = binding.root
        setContentView(view)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gClient = GoogleSignIn.getClient(this, gso);
        initViews()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Timber.d("current user - ${currentUser}")
    }

    private fun initViews() {
        binding.signInButton.setOnClickListener {
            openSomeActivityForResult()
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Timber.d("firebaseAuthWithGoogle: ${account.id}")
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Timber.e("Google sign in failed with message: ${e.message}")
                }
            } else {
                Timber.d("result =${result}")
            }
        }

    private fun openSomeActivityForResult() {
        val signInIntent = gClient.signInIntent
        resultLauncher.launch(signInIntent)
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w( "signInWithCredential:failure ${task.exception}" )
//                    updateUI(null)
                }
            }
    }

}