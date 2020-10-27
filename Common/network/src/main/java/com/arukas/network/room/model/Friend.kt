package com.arukas.network.room.model

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@RealmClass
open class Friend(
    @PrimaryKey
    var objectId: String? = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    var friendId: String? = "",
    var isDeleted: Boolean? = false,
    var userId: String? = ""
) : RealmModel, Parcelable