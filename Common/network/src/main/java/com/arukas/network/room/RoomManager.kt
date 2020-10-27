package com.arukas.network.room

import android.content.Context
import com.arukas.network.model.*

class RoomManager(private val context: Context) {
    private var chatDatabase: ChatDatabase? = null

    companion object {
        private var instance: RoomManager? = null

        @JvmStatic
        fun initial(context: Context) {
            instance = RoomManager(context)
        }

        @JvmStatic
        fun getInstance(): RoomManager {
            if (instance == null) {
                throw Exception("Room Manager doesn't initialized")
            } else {
                return instance!!
            }
        }
    }

    fun setDatabase(databaseName: String) {
        chatDatabase = ChatDatabase.getInstance(context, databaseName)
    }

    fun getFriendById(objectId: String) = chatDatabase?.friendDao()?.getFriendById(objectId)

    fun getSingleById(objectId: String) = chatDatabase?.singleDao()?.getSingleById(objectId)

    fun getFriendByUserId(userId: String) = chatDatabase?.friendDao()?.getFriendByUserId(userId)

    fun getPersonById(objectId: String) = chatDatabase?.personDao()?.getPersonById(objectId)

    fun getPersonByIds(personIds: List<String>) =
        chatDatabase?.personDao()?.getPersonsByIds(personIds)

    fun getPersonWithoutIds(personIds: List<String>) =
        chatDatabase?.personDao()?.getPersonWithoutIds(personIds)

    fun getMemberById(memberId: String) = chatDatabase?.memberDao()?.getMemberById(memberId)

    fun getMemberByUserId(userId: String) = chatDatabase?.memberDao()?.getMemberByUserId(userId)

    fun getSingleByIds(singleIds: List<String>) =
        chatDatabase?.singleDao()?.getSingleListByIds(singleIds)

    fun getDetailById(detailId: String) = chatDatabase?.detailDao()?.getDetailById(detailId)

    fun getMessageById(messageId: String) = chatDatabase?.messageDao()?.getMessageById(messageId)

    fun getLastMessage(roomId: String) = chatDatabase?.messageDao()?.getLastMessage(roomId)

    fun getMyDetail(userId: String, chatId: String) =
        chatDatabase?.detailDao()?.getMyDetail(userId, chatId)

    fun getAllMessage(roomId: String) = chatDatabase?.messageDao()?.getAllMessage(roomId)

    fun setMessage(message: Message) {
        chatDatabase?.messageDao()?.addMessage(message)
    }

    fun setDetail(detail: Detail) {
        chatDatabase?.detailDao()?.addDetail(detail)
    }

    fun setMember(member: Member) {
        chatDatabase?.memberDao()?.addMember(member)
    }

    fun setFriend(friend: Friend) {
        chatDatabase?.friendDao()?.addFriend(friend)
    }

    fun setPerson(person: Person) {
        chatDatabase?.personDao()?.addPerson(person)
    }

    fun setSingle(single: Single) {
        chatDatabase?.singleDao()?.addSingle(single)
    }

    fun setDetails(details: List<Detail>) {
        chatDatabase?.detailDao()?.addDetails(details)
    }

    fun setMembers(members: List<Member>) {
        chatDatabase?.memberDao()?.addMembers(members)
    }
}