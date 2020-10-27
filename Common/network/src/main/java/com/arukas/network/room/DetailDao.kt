package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arukas.network.model.Detail

@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDetail(detail: Detail)

    @Query("select * from detail where detail.objectId=:detailId")
    fun getDetailById(detailId: String): Detail?

    @Query("select * from detail where detail.userId=:userId and detail.chatId=:chatId")
    fun getMyDetail(userId: String, chatId: String): Detail?
}