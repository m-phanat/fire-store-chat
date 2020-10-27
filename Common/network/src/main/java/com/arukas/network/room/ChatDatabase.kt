package com.arukas.network.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arukas.network.model.*

@Database(entities = [Detail::class,Friend::class,Member::class,Message::class,Person::class,Single::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun detailDao():DetailDao
    abstract fun friendDao():FriendDao
    abstract fun memberDao():MemberDao
    abstract fun messageDao():MessageDao
    abstract fun singleDao():SingleDao

    companion object {
        private var instance: ChatDatabase? = null

        fun getInstance(context: Context, databaseName: String): ChatDatabase {
            return if (instance != null && instance!!.openHelper.databaseName == databaseName) {
                instance!!
            } else {
                instance = synchronized(this) {
                    buildDatabase(context, databaseName)
                }

                instance!!
            }
        }

        private fun buildDatabase(context: Context, databaseName: String): ChatDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ChatDatabase::class.java,
                databaseName
            ).build()
        }
    }
}