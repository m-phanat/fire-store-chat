package com.arukas.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arukas.network.model.Person

@Dao
interface PersonDao {
    @Query("select * from person where person.objectId=:personId")
    fun getPersonById(personId:String): Person?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPerson(person: Person)

    @Query("select * from person where person.objectId in (:personIds)")
    fun getPersonsByIds(personIds:List<String>):List<Person>?

    @Query("select * from person where person.objectId not in (:personIds)")
    fun getPersonWithoutIds(personIds: List<String>):List<Person>?
}