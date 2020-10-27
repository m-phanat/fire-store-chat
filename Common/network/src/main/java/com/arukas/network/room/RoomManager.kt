package com.arukas.network.room

import android.content.Context
import com.arukas.network.room.model.Person

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

    fun setDatabase(databaseName:String){
        chatDatabase= ChatDatabase.getInstance(context, databaseName)
    }

    fun getPerson(personId: String): Person?{
        return chatDatabase?.personDao()?.getPersonById(personId)
    }

    fun getOtherPersons(personsIds: List<String>): List<Person>?{
        return chatDatabase?.personDao()?.getPersonsByIds(personsIds)
    }
}