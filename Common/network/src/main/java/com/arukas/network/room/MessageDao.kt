package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arukas.network.model.Message
import io.reactivex.Flowable

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMessage(message: Message)

    @Query("select * from message where message.objectId=:messageId")
    fun getMessageById(messageId: String): Message?

    @Query("select * from message where message.chatId=:roomId order by message.updatedAt desc limit 1")
    fun getLastMessage(roomId: String): Flowable<Message>?

    @Query("select * from message where message.chatId=:roomId order by message.updatedAt asc")
    fun getAllMessage(roomId: String):Flowable<List<Message>>?
}