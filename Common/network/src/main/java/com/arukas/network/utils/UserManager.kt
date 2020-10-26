package com.arukas.network.utils

import com.arukas.network.model.Person
import io.paperdb.Paper

class UserManager {
    companion object {
        private var instance: UserManager? = null
        private const val KEY_BOOK_NAME = "user_book"
        private const val KEY_USER = "user"

        @JvmStatic
        fun getInstance(): UserManager {
            if (instance == null) {
                instance = UserManager()
            }

            return instance!!
        }
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }

    fun setUser(user: Person) {
        Paper.book(KEY_BOOK_NAME).write(KEY_USER, user)
    }

    fun getUser(): Person? {
        return Paper.book(KEY_BOOK_NAME).read<Person?>(KEY_USER, null)
    }

    fun logout() {
        Paper.book(KEY_BOOK_NAME).delete(KEY_USER)
    }
}