package com.arukas.chat.people

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.model.Friend
import com.arukas.network.model.Person
import com.arukas.network.realm.RealmManager
import com.arukas.network.utils.UserManager
import io.reactivex.disposables.Disposable

class PeopleFragmentViewModel(application: Application) : BaseViewModel(application) {
    private val friends = MutableLiveData<List<Person>>()
    private var friendSubscription: Disposable? = null

    fun getFriends(): LiveData<List<Person>> {
        return friends
    }

    fun loadFriend() {
        UserManager.getInstance().getUser()?.objectId?.let { myUserId ->
            friendSubscription = RealmManager.getInstance().getFriends(myUserId)?.subscribe {
                loadFriendData(it)
            }
        }
    }

    private fun loadFriendData(friendList: List<Friend>) {
        val friendIds = friendList.map { it.friendId.orEmpty() }
        val users = RealmManager.getInstance().getFriendsData(friendIds)
        friends.value = users
    }

    override fun onCleared() {
        if (friendSubscription != null && !friendSubscription!!.isDisposed) {
            friendSubscription?.dispose()
            friendSubscription = null
        }
        super.onCleared()
    }
}