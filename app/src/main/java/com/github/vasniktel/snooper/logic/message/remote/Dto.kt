package com.github.vasniktel.snooper.logic.message.remote

import com.github.vasniktel.snooper.logic.model.Location
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.user.remote.RemoteUserDto
import com.github.vasniktel.snooper.logic.user.remote.toModel
import com.github.vasniktel.snooper.logic.user.remote.toRemoteDto
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class RemoteMessageDto(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val description: String? = null,
    val likes: Int,
    val date: Date? = null,
    val owner: RemoteUserDto
)

fun Message.toRemoteDto() =
    RemoteMessageDto(
        id = id,
        latitude = location.latitude,
        longitude = location.longitude,
        address = location.address,
        description = description,
        likes = likes,
        owner = owner.toRemoteDto()
    )

fun RemoteMessageDto.toModel() = Message(
    id = id,
    location = Location(
        latitude = latitude,
        longitude = longitude,
        address = address
    ),
    description = description,
    likes = likes,
    date = date!!,
    owner = owner.toModel()
)
