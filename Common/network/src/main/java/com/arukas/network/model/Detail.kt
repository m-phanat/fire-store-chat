package com.arukas.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
open class Detail(
    @PrimaryKey
    var objectId: String = "",
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
) : Parcelable