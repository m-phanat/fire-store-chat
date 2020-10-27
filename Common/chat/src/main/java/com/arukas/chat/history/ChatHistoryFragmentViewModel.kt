package com.arukas.chat.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.model.Single
import com.arukas.network.room.RoomManager
import com.arukas.network.service.InAppObservable
import com.arukas.network.utils.UserManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChatHistoryFragmentViewModel(application: Application) : BaseViewModel(application) {
    private val chatHistory = MutableLiveData<List<Single>>()
    private val historyUpdate=MutableLiveData<Single>()
    private var memberDisposable: Disposable? = null
    private var singleDisposable: Disposable? = null
    private var lastMessageObservers = mutableListOf<InAppObservable>()

    fun getChatHistory(): LiveData<List<Single>> {
        return chatHistory
    }

    fun getHistoryUpdate():LiveData<Single>{
        return historyUpdate
    }

    fun getMyUserId() = UserManager.getInstance().getUser()?.objectId.orEmpty()

    fun loadChatHistory() {
        if (memberDisposable != null) {
            memberDisposable?.dispose()
            memberDisposable = null
        }

        val myUser = UserManager.getInstance().getUser()

        memberDisposable = RoomManager
            .getInstance()
            .getMemberByUserId(myUser?.objectId.orEmpty())
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { memberResult ->
                if (memberResult.isNotEmpty()) {
                    val roomIds = memberResult.orEmpty().map { it.chatId.orEmpty() }

                    if (roomIds.isNotEmpty()) {
                        loadRoom(roomIds)
                    } else {
                        chatHistory.value = listOf()
                    }
                } else {
                    chatHistory.value = listOf()
                }
            }
    }

    private fun loadRoom(roomIds: List<String>) {
        if (singleDisposable != null) {
            singleDisposable?.dispose()
            singleDisposable = null
        }

        singleDisposable = RoomManager
            .getInstance()
            .getSingleByIds(roomIds)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                if (it.isNotEmpty()) {
                    chatHistory.value = it
                    findLastMessage(it)
                } else {
                    chatHistory.value = listOf()
                }
            }
    }

    private fun findLastMessage(rooms: List<Single>) {
        rooms.forEach { single ->
            val observer = RoomManager
                .getInstance()
                .getLastMessage(single.objectId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe {
                    single.lastMessage = it
                    historyUpdate.value = single
                }

            observer?.let {
                lastMessageObservers.add(InAppObservable(single.chatId.orEmpty(), it))
            }
        }
    }

    override fun onCleared() {
        memberDisposable?.dispose()
        memberDisposable = null

        singleDisposable?.dispose()
        singleDisposable = null

        lastMessageObservers.forEach {
            it.disposable.dispose()
        }
        super.onCleared()
    }
}