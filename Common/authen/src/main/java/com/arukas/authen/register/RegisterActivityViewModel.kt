package com.arukas.authen.register

import android.app.Application
import android.net.Uri
import com.arukas.base.BaseViewModel
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.Person
import com.arukas.network.utils.FireStorageManager
import com.arukas.network.utils.UserManager
import com.google.firebase.auth.FirebaseAuth

class RegisterActivityViewModel(application: Application) : BaseViewModel(application) {
    private var profileUri: Uri? = null
    private val userDb by lazy {
        FireStoreManager
            .getInstance()
            .getDatabase()
            .collection("company_a")
            .document("chat")
            .collection("persons")
    }

    private var onRegisterSuccessListener:((person:Person)->Unit)?=null

    fun setOnRegisterSuccessListener(listener:((person:Person)->Unit)?){
        onRegisterSuccessListener=listener
    }

    fun validateForm(
        username: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String
    ) {
        when {
            username.isBlank() -> {
                postError("Please enter email")
            }

            password.isBlank() -> {
                postError("Please enter password")
            }

            confirmPassword.isBlank() -> {
                postError("Please confirm password")
            }

            password != confirmPassword -> {
                postError("Invalid confirm password")
            }

            firstName.isBlank() -> {
                postError("Please enter first name")
            }

            lastName.isBlank() -> {
                postError("Please enter last name")
            }

            else -> {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userId=it.result?.user?.uid.orEmpty()
                            val person=Person(
                                objectId = userId,
                                email = username,
                                firstName = firstName,
                                lastName = lastName,
                                fullName = "$firstName $lastName",
                                loginMethod = NetworkConstants.LOGIN_EMAIL,
                            )

                            if(profileUri!=null){
                                FireStorageManager.getInstance().uploadUserProfile(userId,profileUri!!,{
                                    addUserToFireStore(person)
                                },{
                                    postError("Can not upload profile image.")
                                })
                            } else {
                                addUserToFireStore(person)
                            }
                        } else {
                            postError("Register failed. Try again later.")
                        }
                    }
            }
        }
    }

    private fun addUserToFireStore(person: Person) {
        userDb.add(person).addOnCompleteListener {
            if(it.isSuccessful){
                UserManager.getInstance().setUser(person)
                onRegisterSuccessListener?.invoke(person)
            } else {
                postError("Sign up failed. Try again later.")
            }
        }
    }

    fun setProfileUri(cropUri: Uri?) {
        profileUri = cropUri
    }
}