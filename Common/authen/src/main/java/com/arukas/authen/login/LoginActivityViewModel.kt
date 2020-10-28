package com.arukas.authen.login

import android.app.Application
import com.arukas.base.BaseViewModel
import com.arukas.network.notification.PushNotification
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.model.Person
import com.arukas.network.utils.UserManager
import com.google.firebase.auth.FirebaseAuth

class LoginActivityViewModel(application: Application) : BaseViewModel(application) {

    private var onLoginSuccess: (() -> Unit)? = null
    private val chatDb by lazy {
        FireStoreManager
            .getInstance()
            .getDatabase()
            .collection("company_a")
            .document("chat")
            .collection("persons")
    }

    fun setOnLoginSuccessListener(listener: (() -> Unit)?) {
        onLoginSuccess = listener
    }

    fun login(username: String, password: String) {
        when {
            username.isBlank() -> {
                postError("Please enter username")
            }
            password.isBlank() -> {
                postError("Please enter password")
            }
            else -> {
                loginFirebase(username, password)
            }
        }
    }

    private fun loginFirebase(username: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loadUserData(username)
                } else {
                    postError("Username or password is not valid. Please try again.")
                }
            }
    }

    private fun loadUserData(username: String) {
        chatDb.whereEqualTo("email", username)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    value?.toObjects(Person::class.java)?.let {
                        if(it.size>0){
                            UserManager.getInstance().setUser(it[0])
                            onLoginSuccess?.invoke()
                            chatDb.document(value.documents[0].id).update("oneSignalId",
                                PushNotification.oneSignalId())
                        } else {
                            postError("Login failed. Try again later.")
                        }
                    }?: kotlin.run {
                        postError("Login failed. Try again later.")
                    }
                } else {
                    postError("Login failed. Try again later.")
                }
            }
    }
}