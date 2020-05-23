package com.github.vasniktel.snooper.logic.model

import android.os.Parcelable
import com.github.vasniktel.snooper.util.SnooperException
import com.google.android.libraries.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    val id: String,
    val name: String,
    val photoUrl: String?
) : Parcelable

fun FirebaseUser.toUser(): User {
    var displayName: String? = displayName

    for (info in providerData) {
        if (displayName != null) break
        displayName = info.displayName
    }

    displayName ?: throw SnooperException("Display name is still null for $this")

    return User(
        id = uid,
        name = displayName,
        photoUrl = photoUrl?.toString()
    )
}

data class Message(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val ownerId: String,
    val ownerName: String,
    val likes: Int,
    val address: String,
    val description: String?,
    val date: Date
) {
    init {
        require(likes >= 0)
    }
}

val Message.latLng get() = LatLng(latitude, longitude)
