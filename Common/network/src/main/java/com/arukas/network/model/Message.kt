package com.arukas.network.model

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@RealmClass
open class Message(
    @PrimaryKey
    var objectId: String? = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    var chatId: String? = "",
    var userId: String? = "",
    var type: String? = "",
    var text: String? = "",
    var isDeleted: Boolean? = false,
) : RealmModel, Parcelable