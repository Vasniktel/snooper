package com.github.vasniktel.snooper.logic.user.remote

import com.github.vasniktel.snooper.logic.model.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteUserDto(
    val id: String,
    val name: String,
    val photoUrl: String?
)

fun User.toRemoteDto() =
    RemoteUserDto(
        id = id,
        name = name,
        photoUrl = photoUrl
    )

fun RemoteUserDto.toModel() = User(
    id = id,
    name = name,
    photoUrl = photoUrl
)
