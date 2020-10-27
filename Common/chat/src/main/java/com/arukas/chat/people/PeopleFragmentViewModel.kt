package com.arukas.chat.people

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.model.Friend
import com.arukas.network.model.Person
import com.arukas.network.room.RoomManager
import com.arukas.network.utils.UserManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PeopleFragmentViewModel(application: Application) : BaseViewModel(application) {
    private val friends = MutableLiveData<List<Person>>()
    private var friendSubscription: Disposable? = null

    fun getFriends(): LiveData<List<Person>> {
        return friends
    }

    fun loadFriend() {
        UserManager.getInstance().getUser()?.objectId?.let { myUserId ->
            friendSubscription = RoomManager
                .getInstance()
                .getFriendByUserId(myUserId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe {
                    loadFriendData(it)
                }
        }
    }

    private fun loadFriendData(friendList: List<Friend>) {
        val friendIds = friendList.map { it.friendId.orEmpty() }
        val users = RoomManager.getInstance().getPersonByIds(friendIds)
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