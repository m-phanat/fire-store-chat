package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arukas.network.model.Single
import io.reactivex.Flowable

@Dao
interface SingleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSingle(single: Single)

    @Query("select * from single where single.objectId=:singleId")
    fun getSingleById(singleId:String): Single?

    @Query("select * from single where single.chatId in (:roomIds) order by single.updatedAt desc")
    fun getSingleListByIds(roomIds: List<String>):Flowable<List<Single>>


}