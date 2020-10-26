package com.arukas.network.realm

import android.content.Context
import com.arukas.network.model.*
import io.reactivex.Flowable
import io.realm.*

class RealmManager(private val context: Context) {
    private var realm: Realm? = null

    companion object {
        private var instance: RealmManager? = null

        @JvmStatic
        fun initial(context: Context) {
            instance = RealmManager(context)
        }

        @JvmStatic
        fun getInstance(): RealmManager {
            if (instance == null) {
                throw Exception("Realm Manager doesn't initialized")
            }

            return instance!!
        }
    }

    fun setRealmDb(databaseName: String) {
        val config = RealmConfiguration.Builder()
            .name("${databaseName}.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.getInstance(config)
    }

    //fun getRealm(): Realm? = realm

    fun getPerson(personId: String): Person? {
        return realm?.where(Person::class.java)?.equalTo("objectId", personId)?.findFirst()
    }

    fun setData(data: RealmModel) {
        realm?.beginTransaction()
        realm?.insertOrUpdate(data)
        realm?.commitTransaction()
    }

    fun setData(data: List<RealmModel>) {
        realm?.beginTransaction()
        realm?.insertOrUpdate(data)
        realm?.commitTransaction()
    }

    fun getFriend(objectId: String): Friend? {
        return realm?.where(Friend::class.java)?.equalTo("objectId", objectId)?.findFirst()
    }

    fun getSingle(singleId: String): Single? {
        return realm?.where(Single::class.java)?.equalTo("objectId", singleId)?.findFirst()
    }

    fun getFriends(userId: String): Flowable<RealmResults<Friend>>? {
        return realm?.where(Friend::class.java)?.equalTo("userId", userId)?.findAll()?.asFlowable()
    }

    fun getFriendsData(friendIds: List<String>): List<Person> {
        return realm?.where(Person::class.java)?.`in`("objectId", friendIds.toTypedArray())
            ?.findAll().orEmpty()
    }

    fun getOtherPersons(friendIds: List<String>): List<Person> {
        return realm?.where(Person::class.java)?.not()?.`in`("objectId", friendIds.toTypedArray())
            ?.findAll().orEmpty()
    }

    fun getMember(memberId: String): Member? {
        return realm?.where(Member::class.java)?.equalTo("objectId", memberId)?.findFirst()
    }

    fun getMembers(userId: String): Flowable<RealmResults<Member>>? {
        return realm?.where(Member::class.java)?.equalTo("userId", userId)?.findAll()?.asFlowable()
    }

    fun getSingleDetails(roomIds: List<String>): Flowable<RealmResults<Single>>? {
        return realm?.where(Single::class.java)?.`in`("objectId", roomIds.toTypedArray())
            ?.sort("updatedAt", Sort.DESCENDING)?.findAll()?.asFlowable()
    }

    fun getDetail(objectId: String): Detail? {
        return realm?.where(Detail::class.java)?.equalTo("objectId", objectId)?.findFirst()
    }

    fun getMessage(objectId: String): Message? {
        return realm?.where(Message::class.java)?.equalTo("objectId", objectId)?.findFirst()
    }

    fun getLastMessages(roomId: String): Flowable<RealmResults<Message>>? {
        return realm?.where(Message::class.java)?.equalTo("chatId", roomId)
            ?.sort("updatedAt", Sort.DESCENDING)?.limit(1)?.findAll()?.asFlowable()
    }

    fun getMyDetail(userId: String, chatId: String): Flowable<RealmResults<Detail>>? {
        return realm?.where(Detail::class.java)?.equalTo("userId", userId)
            ?.equalTo("chatId", chatId)?.findAll()?.asFlowable()
    }

    fun getAllMessage(chatId: String):Flowable<RealmResults<Message>>? {
        return realm?.where(Message::class.java)?.equalTo("chatId",chatId)?.findAll()?.asFlowable()
    }

    fun getMemberInRoom(chatId:String,myUserId:String):Flowable<RealmResults<Member>>?{
        return realm?.where(Member::class.java)?.equalTo("chatId",chatId)?.notEqualTo("userId",myUserId)?.findAll()?.asFlowable()
    }
}