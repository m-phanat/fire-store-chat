package com.arukas.chat.user_profile

import android.app.Application
import com.arukas.base.BaseViewModel
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.Detail
import com.arukas.network.model.Member
import com.arukas.network.model.Person
import com.arukas.network.model.Single
import com.arukas.network.utils.FireStorageManager
import com.arukas.network.utils.UserManager
import com.google.firebase.storage.StorageReference
import java.util.*

class UserProfileActivityViewModel(application: Application) : BaseViewModel(application) {
    private var user: Person? = null
    private val chatDb by lazy {
        FireStoreManager
            .getInstance()
            .getDatabase()
            .collection(NetworkConstants.COLLECTION_COMPANY)
            .document(NetworkConstants.DOCUMENT_CHAT)
    }

    fun setUser(user: Person) {
        this.user = user
    }

    fun getUserProfileImage(): StorageReference {
        return FireStorageManager.getInstance().getUserImageReference(user?.objectId.orEmpty())
    }

    fun getDisplayName(): String {
        return user?.fullName.orEmpty()
    }

    fun createChatRoom(onRoomCreated: ((room: Single) -> Unit)? = null) {
        UserManager.getInstance().getUser()?.let { myUser ->
            findRoom(myUser.objectId.orEmpty(), user?.objectId.orEmpty()) { room1 ->
                if (room1 != null) {
                    onRoomCreated?.invoke(room1)
                } else {
                    findRoom(user?.objectId.orEmpty(), myUser.objectId.orEmpty()) { room2 ->
                        if (room2 != null) {
                            onRoomCreated?.invoke(room2)
                        } else {
                            createNewRoom(myUser, onRoomCreated)
                        }
                    }
                }
            }
        }
    }

    private fun createNewRoom(myUser: Person, onRoomCreated: ((room: Single) -> Unit)? = null) {
        val currentTs = Calendar.getInstance().timeInMillis
        val chatId = chatDb.collection(NetworkConstants.COLLECTION_SINGLE).document().id
        val room = Single(
            objectId = chatId,
            chatId = chatId,
            fullName1 = myUser.fullName.orEmpty(),
            fullName2 = user?.fullName.orEmpty(),
            userId1 = myUser.objectId.orEmpty(),
            userId2 = user?.objectId.orEmpty(),
            createdAt = currentTs,
            updatedAt = currentTs
        )

        chatDb.collection(NetworkConstants.COLLECTION_SINGLE)
            .document(chatId)
            .set(room)
            .addOnSuccessListener {

            }
            .addOnSuccessListener {
                val timestamp = Calendar.getInstance().timeInMillis

                joinRoom(chatId, myUser.objectId.orEmpty(), true, timestamp)
                joinRoom(chatId, user?.objectId.orEmpty(), false, timestamp)

                onRoomCreated?.invoke(room)
            }
    }

    private fun findRoom(
        userId1: String,
        userId2: String,
        onComplete: ((room: Single?) -> Unit)? = null
    ) {
        chatDb.collection(NetworkConstants.COLLECTION_SINGLE)
            .whereEqualTo("userId1", userId1)
            .whereEqualTo("userId2", userId2)
            .get()
            .addOnSuccessListener {
                if (it.size() > 0) {
                    val rooms = it.toObjects(Single::class.java)
                    onComplete?.invoke(rooms[0])
                } else {
                    onComplete?.invoke(null)
                }
            }
    }

    private fun joinRoom(roomId: String, userId: String, isActive: Boolean, timestamp: Long) {
        val memberId = chatDb.collection(NetworkConstants.COLLECTION_MEMBER).document().id
        val roomMember = Member(
            objectId = memberId,
            chatId = roomId,
            isActive = isActive,
            createdAt = timestamp,
            updatedAt = timestamp,
            userId = userId
        )

        chatDb.collection(NetworkConstants.COLLECTION_MEMBER).add(roomMember).addOnSuccessListener {
            createChatDetail(userId, roomId, timestamp)
        }
    }

    private fun createChatDetail(userId: String, roomId: String, timestamp: Long) {
        val detailId = chatDb.collection(NetworkConstants.COLLECTION_DETAIL).document().id
        val chatDetail = Detail(
            objectId = detailId,
            chatId = roomId,
            userId = userId,
            createdAt = timestamp,
            lastRead = timestamp,
            updatedAt = timestamp
        )

        chatDb.collection(NetworkConstants.COLLECTION_DETAIL).add(chatDetail)
    }
}