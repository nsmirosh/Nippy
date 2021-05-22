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
import timber.log.Timber

class AuthActivity : AppCompatActivity() {

    private lateinit var gClient: GoogleSignInClient
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gClient = GoogleSignIn.getClient(this, gso);
        initViews()
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
                    Timber.d("firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
//                Timber.e( "Google sign in failed", e)
                }
            }
        }

    fun openSomeActivityForResult() {
        val signInIntent = gClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
}