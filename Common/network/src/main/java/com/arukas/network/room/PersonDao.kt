package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Query
import com.arukas.network.room.model.Person
import io.reactivex.Flowable

@Dao
interface PersonDao {
    @Query("select * from person where person.objectId=:personId")
    fun getPersonById(personId:String):Person

    fun setPerson(person:Person)

    @Query("select * from person where person.objectId in (:personIds)")
    fun getPersonsByIds(personIds:List<String>):List<Person>
}