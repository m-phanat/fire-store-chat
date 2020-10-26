package com.arukas.chat.room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arukas.base.BaseViewModel
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.Detail
import com.arukas.network.model.Message
import com.arukas.network.model.Single
import com.arukas.network.realm.RealmManager
import com.arukas.network.utils.UserManager
import io.reactivex.disposables.Disposable
import java.util.*

class ChatRoomActivityViewModel(application: Application) : BaseViewModel(application) {
    private val messages = MutableLiveData<List<Message>>()
    private var detailDisposable: Disposable? = null
    private var messageDisposable: Disposable? = null

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
            RealmManager.getInstance().getMyDetail(getMyUserId(), room?.chatId.orEmpty())
                ?.subscribe {
                    val details = it.toList()
                    if (details.isNotEmpty()) {
                        roomDetail = Detail(
                            details[0].objectId,
                            details[0].neverSync,
                            details[0].requireSync,
                            details[0].createdAt,
                            details[0].updatedAt,
                            details[0].chatId,
                            details[0].isArchived,
                            details[0].isDeleted,
                            details[0].lastRead,
                            details[0].mutedUntil,
                            details[0].typing,
                            details[0].userId
                        )
                        loadMessages()
                    }
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

    private fun loadMessages() {
        messageDisposable =
            RealmManager.getInstance().getAllMessage(room?.chatId.orEmpty())?.subscribe {
                val messages = it.toList()
                this.messages.value = messages

                if (messages.isNotEmpty()) {
                    val lastMessage = messages[0]
                    val timestamp = Calendar.getInstance().timeInMillis

                    roomDetail?.run {
                        updatedAt = timestamp
                        lastRead = lastMessage.updatedAt

                        chatDb.collection("details").document(roomDetail?.objectId.orEmpty())
                            .set(this)
                    }
                }
            }
        /*chatDb.collection(NetworkConstants.COLLECTION_MESSAGE)
            .whereEqualTo("chatId", room?.chatId)
            .orderBy("updatedAt", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    val messages = value?.toObjects(Message::class.java).orEmpty()
                    this.messages.value = messages

                    if (messages.isNotEmpty()) {
                        val lastMessage = messages[0]
                        val timestamp = Calendar.getInstance().timeInMillis
                        roomDetail?.run {
                            updatedAt = timestamp
                            lastRead = lastMessage.updatedAt
                            chatDb.collection("details").document(roomDetailId).set(this)
                        }
                    }
                }
            }*/
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

        chatDb
            .collection(NetworkConstants.COLLECTION_MESSAGE)
            .add(message)
            .addOnSuccessListener {
                updateChatDetail(timestamp)
                updateRoom(timestamp)
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