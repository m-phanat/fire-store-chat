package com.arukas.network.room.model

import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : Parcelable