package com.arukas.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
open class Friend(
    @PrimaryKey
    var objectId: String = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    var friendId: String? = "",
    var isDeleted: Boolean? = false,
    var userId: String? = ""
) : Parcelable