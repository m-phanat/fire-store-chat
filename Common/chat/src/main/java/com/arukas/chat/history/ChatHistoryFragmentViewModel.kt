package com.arukas.chat.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.Single
import com.arukas.network.realm.RealmManager
import com.arukas.network.service.InAppObservable
import com.arukas.network.utils.UserManager
import io.reactivex.disposables.Disposable

class ChatHistoryFragmentViewModel(application: Application) : BaseViewModel(application) {
    private val chatHistory = MutableLiveData<List<Single>>()
    private var memberDisposable: Disposable? = null
    private var singleDisposable: Disposable? = null
    private var lastMessageObservers = mutableListOf<InAppObservable>()

    fun getChatHistory(): LiveData<List<Single>> {
        return chatHistory
    }

    private val chatDb by lazy {
        FireStoreManager.getInstance()
            .getDatabase()
            .collection(NetworkConstants.COLLECTION_COMPANY)
            .document(NetworkConstants.DOCUMENT_CHAT)
    }

    fun getMyUserId() = UserManager.getInstance().getUser()?.objectId.orEmpty()

    fun loadChatHistory() {
        if (memberDisposable != null) {
            memberDisposable?.dispose()
            memberDisposable = null
        }

        val myUser = UserManager.getInstance().getUser()

        memberDisposable = RealmManager.getInstance().getMembers(myUser?.objectId.orEmpty())
            ?.subscribe { memberResult ->
                if (memberResult.size > 0) {
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

        singleDisposable = RealmManager.getInstance().getSingleDetails(roomIds)?.subscribe {
            if (it.size > 0) {
                findLastMessage(it.toList())
            } else {
                chatHistory.value = listOf()
            }
        }
    }

    private fun findLastMessage(rooms: List<Single>) {
        /*if (rooms.isNotEmpty()) {
            *//*val updateTimes = rooms.map { it.updatedAt ?: 0L }
            chatDb.collection(NetworkConstants.COLLECTION_MESSAGE)
                .whereIn("updatedAt", updateTimes)
                .get()
                .addOnSuccessListener { result ->
                    val messages = result.toObjects(Message::class.java).orEmpty()

                    messages.forEach { message ->
                        val room = rooms.find { it.chatId == message.chatId }

                        if (room != null)
                            room.lastMessage = message
                    }

                    chatHistory.value = rooms
                }*//*
        }*/

        rooms.forEach { single ->
            val observer =
                RealmManager.getInstance().getLastMessages(single.chatId.orEmpty())?.subscribe {
                    val messages = it.toList()
                    if (messages.isNotEmpty()) {
                        single.lastMessage = messages[0]
                        chatHistory.value = rooms
                    }
                }

            observer?.let {
                lastMessageObservers.add(InAppObservable(single.chatId.orEmpty(), it))
            }
        }

        chatHistory.value = rooms
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