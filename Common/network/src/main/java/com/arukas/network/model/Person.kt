package com.arukas.network.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

@RealmClass
@Parcelize
open class Person(
    @PrimaryKey
    var objectId: String? = "",
    var neverSync: Boolean? = false,
    var requireSync: Boolean? = false,
    var createdAt: Long? = 0L,
    var updatedAt: Long? = 0L,

    @SerializedName("country")
    var country: String? = "",

    @SerializedName("firstname")
    var firstName: String? = "",

    @SerializedName("lastActive")
    var lastActive: Int? = 0,

    @SerializedName("wallpaper")
    var wallpaper: String? = "",

    @SerializedName("loginMethod")
    var loginMethod: String? = "",

    @SerializedName("lastTerminate")
    var lastTerminate: Int? = 0,

    @SerializedName("keepMedia")
    var keepMedia: Int? = 0,

    @SerializedName("lastname")
    var lastName: String? = "",

    @SerializedName("oneSignalId")
    var oneSignalId: String? = "",

    @SerializedName("phone")
    var phone: String? = "",

    @SerializedName("networkAudio")
    var networkAudio: Int? = 0,

    @SerializedName("location")
    var location: String? = "",

    @SerializedName("networkPhoto")
    var networkPhoto: Int? = 0,

    @SerializedName("fullname")
    var fullName: String? = "",

    @SerializedName("pictureAt")
    var pictureAt: Int? = 0,

    @SerializedName("email")
    var email: String? = "",

    @SerializedName("networkVideo")
    var networkVideo: Int? = 0,

    @SerializedName("status")
    var status: String? = "",

    @Ignore
    @Exclude
    var isAdded: Boolean = false
) : RealmModel, Parcelable