package com.arukas.network.utils

import android.net.Uri
import com.arukas.network.constants.NetworkConstants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class FireStorageManager {

    companion object {
        private var instance: FireStorageManager? = null

        @JvmStatic
        fun getInstance(): FireStorageManager {
            if (instance == null)
                instance = FireStorageManager()

            return instance!!
        }
    }

    fun getUserImageReference(userId: String): StorageReference {
        val path = NetworkConstants.USER_PROFILE_DIR + File.separator + userId + ".png"
        return FirebaseStorage.getInstance().reference.child(path)
    }

    fun uploadUserProfile(
        userId: String,
        fileUri: Uri,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        getUserImageReference(userId).putFile(fileUri).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess?.invoke()
            } else {
                onError?.invoke()
            }
        }
    }


}