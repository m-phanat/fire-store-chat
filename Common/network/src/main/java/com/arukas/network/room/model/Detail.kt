package com.arukas.network.room.model

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@RealmClass
open class Detail(
    @PrimaryKey
    var objectId: String? = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    var chatId: String? = "",
    var isArchived: Boolean? = false,
    var isDeleted: Boolean? = false,
    var lastRead: Long? = 0L,
    var mutedUntil: Int? = 0,
    var typing: Boolean? = false,
    var userId: String? = ""
) : RealmModel, Parcelable