package com.arukas.network.cloud

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FireStoreManager {
    companion object {
        private var instance: FireStoreManager? = null

        @JvmStatic
        fun initial() {
            instance = FireStoreManager()
        }

        @JvmStatic
        fun getInstance(): FireStoreManager {
            if (instance == null) {
                throw Exception("Fire Store manager doesn't initialized")
            }

            return instance!!
        }
    }

    private val fireStoreDb = FirebaseFirestore.getInstance()

    init {
        val setting = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED).build()
        fireStoreDb.firestoreSettings = setting
    }

    fun getDatabase():FirebaseFirestore{
        return fireStoreDb
    }
}