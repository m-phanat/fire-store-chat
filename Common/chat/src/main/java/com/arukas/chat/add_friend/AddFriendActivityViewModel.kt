package com.arukas.chat.add_friend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.Friend
import com.arukas.network.model.Person
import com.arukas.network.realm.RealmManager
import com.arukas.network.utils.UserManager
import io.reactivex.disposables.Disposable
import java.util.*

class AddFriendActivityViewModel(application: Application) : BaseViewModel(application) {
    private val users = MutableLiveData<List<Person>>()
    private var friendSubscription:Disposable?=null
    private var keyword = ""

    fun setKeyword(keyword: String) {
        this.keyword = keyword
    }

    fun getUsers(): LiveData<List<Person>> {
        return users
    }

    private val chatDb by lazy {
        FireStoreManager.getInstance()
            .getDatabase()
            .collection(NetworkConstants.COLLECTION_COMPANY)
            .document(NetworkConstants.DOCUMENT_CHAT)
    }

    fun loadUser() {
        val myUser = UserManager.getInstance().getUser()
        val userIds = mutableListOf(myUser?.objectId.orEmpty())
        friendSubscription=RealmManager.getInstance().getFriends(myUser?.objectId.orEmpty())?.subscribe { friendList ->
            val friendIds = friendList.map { it.friendId.orEmpty() }
            userIds.addAll(friendIds)

            loadOtherUsers(userIds)
        }

        /*chatDb.collection("friends")
            .whereEqualTo("userId", myUser?.objectId.orEmpty())
            .get()
            .addOnCompleteListener { friendResult ->
                if (friendResult.isSuccessful) {
                    val friendList = friendResult.result?.toObjects(Friend::class.java).orEmpty()
                    val friendIds = friendList.map { it.friendId.orEmpty() }
                    userIds.addAll(friendIds)

                    loadOtherUsers(userIds)
                } else {
                    users.value = listOf()
                }
            }*/
    }

    private fun loadOtherUsers(friendIds: List<String>) {
        val allUser = RealmManager.getInstance().getOtherPersons(friendIds)
        if (keyword.isNotBlank()) {
            val resultUser =
                allUser.filter { it.email != null && it.email!!.contains(keyword) }
            users.value = resultUser
        } else {
            users.value = allUser
        }
    }

    fun addFriend(user: Person, onSuccessListener: ((user: Person) -> Unit)? = null) {
        val myUserId = UserManager.getInstance().getUser()?.objectId.orEmpty()
        chatDb.collection(NetworkConstants.COLLECTION_FRIEND)
            .whereEqualTo("friendId", user.objectId)
            .whereEqualTo("userId", myUserId)
            .get()
            .addOnCompleteListener { friendResult ->
                if (friendResult.isSuccessful) {
                    val result = friendResult.result?.toObjects(Friend::class.java)
                    if (result.isNullOrEmpty()) {
                        val currentTime = Calendar.getInstance().timeInMillis
                        UserManager.getInstance().getUser()?.let { myUser ->
                            val objectId = chatDb.collection(NetworkConstants.COLLECTION_FRIEND).document().id
                            val friend = Friend(
                                objectId = objectId,
                                userId = myUser.objectId.orEmpty(),
                                friendId = user.objectId.orEmpty(),
                                createdAt = currentTime,
                                updatedAt = currentTime
                            )

                            chatDb.collection(NetworkConstants.COLLECTION_FRIEND).document(objectId).set(friend)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        onSuccessListener?.invoke(user)
                                    } else {
                                        postError("Can not add friend. Try again later.")
                                    }
                                }
                        }
                    } else {
                        postError("Already added")
                    }
                } else {
                    postError("Can not add friend. Try again later.")
                }
            }
    }

}