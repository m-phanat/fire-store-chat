package com.arukas.chat.room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.Detail
import com.arukas.network.model.Message
import com.arukas.network.model.Person
import com.arukas.network.model.Single
import com.arukas.network.notification.PushNotification
import com.arukas.network.room.RoomManager
import com.arukas.network.utils.UserManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ChatRoomActivityViewModel(application: Application) : BaseViewModel(application) {
    private val messages = MutableLiveData<List<Message>>()
    private var detailDisposable: Disposable? = null
    private var messageDisposable: Disposable? = null
    private var memberDisposable: Disposable? = null

    private var room: Single? = null
    private var roomDetail: Detail? = null
    private val chatDb by lazy {
        FireStoreManager
                .getInstance()
                .getDatabase()
                .collection(NetworkConstants.COLLECTION_COMPANY)
                .document(NetworkConstants.DOCUMENT_CHAT)
    }

    fun getMessages(): LiveData<List<Message>> {
        return messages
    }

    fun loadRoomDetail() {
        detailDisposable =
                RoomManager.getInstance().getMyDetail(getMyUserId(), room?.objectId.orEmpty())
                        ?.subscribe {
                            roomDetail = it
                        }
    }

    fun setRoom(room: Single) {
        this.room = room
    }

    private fun getRecipientId(): String {
        val myUserId = getMyUserId()
        return if (room?.userId1.orEmpty() == myUserId) room?.userId2.orEmpty() else room?.userId1.orEmpty()
    }

    private fun getMyUserId(): String {
        return UserManager.getInstance().getUser()?.objectId.orEmpty()
    }

    fun getRoomName(): String {
        val myUserId = getMyUserId()
        return if (myUserId == room?.userId1.orEmpty()) room?.fullName2.orEmpty() else room?.fullName1.orEmpty()
    }

    fun loadMessages() {
        messageDisposable =
                RoomManager
                        .getInstance()
                        .getAllMessage(room?.objectId.orEmpty())
                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe {
                            this.messages.value = it

                            if (it.isNotEmpty()) {
                                val lastMessage = it[0]
                                val timestamp = Calendar.getInstance().timeInMillis

                                roomDetail?.run {
                                    updatedAt = timestamp
                                    lastRead = lastMessage.updatedAt

                                    chatDb.collection("details").document(roomDetail?.objectId.orEmpty())
                                            .set(this)
                                }
                            }
                        }
    }

    fun sendTextMessage(text: String) {
        val timestamp = Calendar.getInstance().timeInMillis
        val messageId = chatDb.collection(NetworkConstants.COLLECTION_MESSAGE).document().id
        val message = Message(
                objectId = messageId,
                chatId = roomDetail?.chatId.orEmpty(),
                userId = getMyUserId(),
                type = "text",
                text = text,
                isDeleted = false,
                createdAt = timestamp,
                updatedAt = timestamp
        )

        chatDb.collection(NetworkConstants.COLLECTION_MESSAGE)
                .add(message)
                .addOnSuccessListener {
                    updateChatDetail(timestamp)
                    updateRoom(timestamp)
                }

        val roomdb = RoomManager.getInstance()
        val userId = mutableListOf<String>()

        memberDisposable = roomdb.getMemberInRoom(roomDetail?.chatId.toString(), roomDetail?.userId.toString())?.subscribe {
            it.forEach { r ->
                chatDb.collection(NetworkConstants.COLLECTION_PERSON).whereEqualTo("objectId", r.userId)
                        .get()
                        .addOnSuccessListener { q ->
                            q.toObjects(Person::class.java).let { p ->
                                p[0].oneSignalId?.let { id ->
                                    userId.add(id)
                                    PushNotification.send(id, text)
                                }
                            }
                        }
            }
        }


    }

    private fun updateChatDetail(timestamp: Long) {
        roomDetail?.run {
            lastRead = timestamp
            updatedAt = timestamp
            chatDb.collection(NetworkConstants.COLLECTION_DETAIL)
                    .document(roomDetail?.objectId.orEmpty()).set(this)
        }
    }

    private fun updateRoom(timestamp: Long) {
        room?.run {
            updatedAt = timestamp
            chatDb.collection(NetworkConstants.COLLECTION_SINGLE).document(chatId.orEmpty())
                    .set(this)
        }
    }

    override fun onCleared() {
        detailDisposable?.dispose()
        detailDisposable = null

        messageDisposable?.dispose()
        messageDisposable = null
        super.onCleared()
    }
}