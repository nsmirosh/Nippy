package apps.smoll.dragdropgame.utils.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class FirebaseUtils(
    val fireStoreInstance: FirebaseFirestore = Firebase.firestore,
    val authInstance: FirebaseAuth = Firebase.auth
) {

    open fun isAuthenticated() =
        authInstance.currentUser != null
}