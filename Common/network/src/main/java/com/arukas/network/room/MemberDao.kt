package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arukas.network.model.Member
import io.reactivex.Flowable

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMember(member: Member)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMembers(members:List<Member>)

    @Query("select * from member where member.objectId=:memberId")
    fun getMemberById(memberId: String): Member?

    @Query("select * from member where member.userId=:userId")
    fun getMemberByUserId(userId: String): Flowable<List<Member>>?

    @Query("select * from member where member.chatId=:chatId and member.userId!=:myUserId")
    fun getMemberInRoom(chatId: String, myUserId: String): Flowable<List<Member>>?
}