package apps.smoll.dragdropgame.utils.firestoreAuth

import com.google.firebase.auth.FirebaseAuth

open class FirebaseAuthUtils(val firebaseAuth: FirebaseAuth) {

    open fun isAuthenticated() =
        firebaseAuth.currentUser != null
}