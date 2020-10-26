package com.arukas.network.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.arukas.network.cloud.FireStoreManager
import com.arukas.network.constants.NetworkConstants
import com.arukas.network.model.*
import com.arukas.network.realm.RealmManager
import com.arukas.network.utils.UserManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.disposables.Disposable

class FireStoreObserverService : Service() {
    private var personListener: ListenerRegistration? = null
    private var friendListener: ListenerRegistration? = null
    private var single1Listener: ListenerRegistration? = null
    private var single2Listener: ListenerRegistration? = null
    private var memberListener: ListenerRegistration? = null
    private var memberDisposable: Disposable? = null
    private var listeners = mutableListOf<RemoteObservable>()


    private var chatDb: DocumentReference? = null

    companion object {
        @JvmStatic
        fun createIntent(context: Context, companyName: String): Intent {
            return Intent(context, FireStoreObserverService::class.java).apply {
                putExtra("company_name", companyName)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("company_name")?.let {
            chatDb = FireStoreManager
                .getInstance()
                .getDatabase()
                .collection(it)
                .document(NetworkConstants.DOCUMENT_CHAT)

            RealmManager.getInstance().setRealmDb(it)

            observePersons()
            observeFriends()
            observeSingle1()
            observeSingle2()
            observeMember()
            observeActiveRoom()
        }
        return START_STICKY
    }

    private fun observeActiveRoom() {
        memberDisposable?.dispose()
        memberDisposable = null

        val myUserId = UserManager.getInstance().getUser()?.objectId.orEmpty()
        memberDisposable =
            RealmManager.getInstance().getMembers(myUserId)?.subscribe { memberResult ->
                if (memberResult.size > 0) {
                    for (member in memberResult) {
                        if (member.isActive == true) {
                            //add observer
                            observeMemberDependencies(member)
                        } else {
                            //remove observer
                            removeMemberDependencies(member)
                        }
                    }
                }
            }
    }

    private fun removeMemberDependencies(member: Member) {
        //remove in-app database observer

        val listenerList = listeners.filter { it.chatId == member.chatId }
        listenerList.forEach {
            it.listener?.remove()
            listeners.remove(it)
        }
    }

    private fun observeMemberDependencies(member: Member?) {
        observeMember(member?.chatId.orEmpty())
        observeDetail(member?.chatId.orEmpty())
        observeMessage(member?.chatId.orEmpty())
    }

    private fun observeMessage(chatId: String) {
        val listener =
            chatDb?.collection(NetworkConstants.COLLECTION_MESSAGE)?.whereEqualTo("chatId", chatId)
                ?.addSnapshotListener { value, error ->
                    if (error == null) {
                        value?.documentChanges?.forEach {
                            val remoteMessage = it.document.toObject(Message::class.java)
                            val localeMessage = RealmManager.getInstance()
                                .getMessage(remoteMessage.objectId.orEmpty())

                            val remoteUpdatedAt = remoteMessage.updatedAt ?: 0L
                            val localeUpdatedAt = localeMessage?.updatedAt ?: 0L

                            if (localeMessage == null || remoteUpdatedAt > localeUpdatedAt) {
                                RealmManager.getInstance().setData(remoteMessage)
                            }
                        }
                    }
                }

        listener?.let {
            val remoteObservable = RemoteObservable(chatId, listener)
            listeners.add(remoteObservable)
        }
    }

    private fun observeDetail(chatId: String) {
        val listener =
            chatDb?.collection(NetworkConstants.COLLECTION_DETAIL)?.whereEqualTo("chatId", chatId)
                ?.addSnapshotListener { value, error ->
                    if (error == null) {
                        value?.documentChanges?.forEach {
                            val remoteDetail = it.document.toObject(Detail::class.java)
                            val localeDetail = RealmManager.getInstance()
                                .getDetail(remoteDetail.objectId.orEmpty())

                            val remoteUpdatedAt = remoteDetail.updatedAt ?: 0L
                            val localeUpdatedAt = localeDetail?.updatedAt ?: 0L

                            if (localeDetail == null || remoteUpdatedAt > localeUpdatedAt) {
                                RealmManager.getInstance().setData(remoteDetail)
                            }
                        }
                    }
                }
        listener?.let {
            val remoteObservable = RemoteObservable(chatId, listener)
            listeners.add(remoteObservable)
        }
    }

    private fun observeMember(chatId: String) {
        val listener =
            chatDb?.collection(NetworkConstants.COLLECTION_MEMBER)?.whereEqualTo("chatId", chatId)
                ?.addSnapshotListener { value, error ->
                    if (error == null) {
                        value?.documentChanges?.forEach {
                            val remoteMember = it.document.toObject(Member::class.java)
                            val localeMember = RealmManager.getInstance()
                                .getMember(remoteMember.objectId.orEmpty())

                            val remoteUpdatedAt = remoteMember.updatedAt ?: 0L
                            val localeUpdatedAt = localeMember?.updatedAt ?: 0L

                            if (localeMember == null || remoteUpdatedAt > localeUpdatedAt) {
                                RealmManager.getInstance().setData(remoteMember)
                            }
                        }
                    }
                }

        listener?.let {
            val remoteObservable = RemoteObservable(chatId, listener)
            listeners.add(remoteObservable)
        }
    }

    private fun observeMember() {
        val myUserId = UserManager.getInstance().getUser()?.objectId.orEmpty()
        memberListener =
            chatDb?.collection(NetworkConstants.COLLECTION_MEMBER)?.whereEqualTo("userId", myUserId)
                ?.addSnapshotListener { value, error ->
                    if (error == null) {
                        value?.documentChanges?.forEach {
                            val remoteMember = it.document.toObject(Member::class.java)
                            val localeMember =
                                RealmManager.getInstance()
                                    .getMember(remoteMember.objectId.orEmpty())

                            val remoteUpdatedAt = remoteMember.updatedAt ?: 0L
                            val localeUpdatedAt = localeMember?.updatedAt ?: 0L

                            if (localeMember == null || remoteUpdatedAt > localeUpdatedAt) {
                                RealmManager.getInstance().setData(remoteMember)
                            }
                        }
                    }
                }
    }

    override fun onCreate() {
        super.onCreate()
    }

    private fun observeSingle2() {
        val myUserId = UserManager.getInstance().getUser()?.objectId.orEmpty()
        single2Listener = chatDb?.collection(NetworkConstants.COLLECTION_SINGLE)
            ?.whereEqualTo("userId2", myUserId)
            ?.addSnapshotListener { value, error ->
                if (error == null) {
                    value?.documentChanges?.forEach {
                        val remoteSingle = it.document.toObject(Single::class.java)
                        val localeSingle =
                            RealmManager.getInstance().getSingle(remoteSingle.objectId.orEmpty())

                        val remoteUpdatedAt = remoteSingle.updatedAt ?: 0L
                        val localeUpdatedAt = localeSingle?.updatedAt ?: 0L

                        if (localeSingle == null || remoteUpdatedAt > localeUpdatedAt) {
                            RealmManager.getInstance().setData(remoteSingle)
                            getMember(remoteSingle.chatId.orEmpty())
                            getDetail(remoteSingle.chatId.orEmpty())
                        }
                    }
                }
            }
    }

    private fun observeSingle1() {
        val myUserId = UserManager.getInstance().getUser()?.objectId.orEmpty()
        single1Listener = chatDb?.collection(NetworkConstants.COLLECTION_SINGLE)
            ?.whereEqualTo("userId1", myUserId)
            ?.addSnapshotListener { value, error ->
                if (error == null) {
                    value?.documentChanges?.forEach {
                        val remoteSingle = it.document.toObject(Single::class.java)
                        val localeSingle =
                            RealmManager.getInstance().getSingle(remoteSingle.objectId.orEmpty())

                        val remoteUpdatedAt = remoteSingle.updatedAt ?: 0L
                        val localeUpdatedAt = localeSingle?.updatedAt ?: 0L

                        if (localeSingle == null || remoteUpdatedAt > localeUpdatedAt) {
                            RealmManager.getInstance().setData(remoteSingle)
                            getMember(remoteSingle.chatId.orEmpty())
                            getDetail(remoteSingle.chatId.orEmpty())
                        }
                    }
                }
            }
    }

    private fun getDetail(chatId: String) {
        chatDb?.collection(NetworkConstants.COLLECTION_DETAIL)?.whereEqualTo("chatId", chatId)
            ?.get()?.addOnSuccessListener {
                val details = it.toObjects(Detail::class.java)
                if (details.isNotEmpty()) {
                    RealmManager.getInstance().setData(details)
                }
            }
    }

    private fun getMember(chatId: String) {
        chatDb?.collection(NetworkConstants.COLLECTION_MEMBER)?.whereEqualTo("chatId", chatId)
            ?.get()?.addOnSuccessListener {
                val members = it.toObjects(Member::class.java)
                if (members.isNotEmpty()) {
                    RealmManager.getInstance().setData(members)
                }
            }
    }

    private fun observeFriends() {
        val myUserId = UserManager.getInstance().getUser()?.objectId.orEmpty()
        friendListener =
            chatDb?.collection(NetworkConstants.COLLECTION_FRIEND)?.whereEqualTo("userId", myUserId)
                ?.addSnapshotListener { value, error ->
                    if (error == null) {
                        value?.documentChanges?.forEach {
                            val remoteFriend = it.document.toObject(Friend::class.java)
                            val localeFriend =
                                RealmManager.getInstance()
                                    .getFriend(remoteFriend.objectId.orEmpty())

                            val remoteUpdatedAt = remoteFriend.updatedAt ?: 0L
                            val localeUpdatedAt = localeFriend?.updatedAt ?: 0L

                            if (localeFriend == null || remoteUpdatedAt > localeUpdatedAt) {
                                RealmManager.getInstance().setData(remoteFriend)
                            }
                        }
                    }
                }
    }

    private fun observePersons() {
        personListener = chatDb?.collection(NetworkConstants.COLLECTION_PERSON)
            ?.addSnapshotListener { value, error ->
                if (error == null) {
                    value?.documentChanges?.forEach {
                        val person = it.document.toObject(Person::class.java)
                        val localPerson =
                            RealmManager.getInstance().getPerson(person.objectId.orEmpty())

                        val remoteUpdatedAt = person.updatedAt ?: 0L
                        val localeUpdatedAt = localPerson?.updatedAt ?: 0L

                        if (localPerson == null || remoteUpdatedAt > localeUpdatedAt) {
                            RealmManager.getInstance().setData(person)
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        personListener?.remove()
        friendListener?.remove()
        single1Listener?.remove()
        single2Listener?.remove()
        memberListener?.remove()
        memberDisposable?.dispose()
        memberDisposable = null
        removeMemberDependencies()
        super.onDestroy()
    }

    private fun removeMemberDependencies() {
        listeners.forEach {
            it.listener?.remove()
        }
    }
}

data class InAppObservable(var chatId: String, var disposable: Disposable)

data class RemoteObservable(var chatId: String, var listener: ListenerRegistration?)