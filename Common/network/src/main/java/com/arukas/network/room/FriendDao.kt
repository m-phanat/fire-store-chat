package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arukas.network.model.Friend
import io.reactivex.Flowable

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFriend(friend: Friend)

    @Query("select * from friend where friend.objectId=:objectId")
    fun getFriendById(objectId: String): Friend?

    @Query("select * from friend where friend.userId=:userId")
    fun getFriendByUserId(userId:String):Flowable<List<Friend>>?
}