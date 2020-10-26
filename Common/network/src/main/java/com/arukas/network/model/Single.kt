package com.arukas.network.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@RealmClass
open class Single(
    @PrimaryKey
    var objectId: String? = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    var chatId: String? = "",
    var userId1: String? = "",
    var fullName1: String? = "",
    var initials1: String? = "",
    var pictureAt1: String? = "",

    var userId2: String? = "",
    var fullName2: String? = "",
    var initials2: String? = "",
    var pictureAt2: String? = "",

    @Ignore
    @Exclude
    var lastMessage: Message? = null,
    @Ignore
    @Exclude
    var unreadCount: Int? = 0
) : RealmModel, Parcelable