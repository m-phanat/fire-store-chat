package com.arukas.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
open class Member(
    @PrimaryKey
    var objectId: String = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    var chatId: String? = "",
    var isActive: Boolean? = true,
    var userId: String? = ""
) : Parcelable