package com.github.vasniktel.snooper.logic.message.remote

import com.github.vasniktel.snooper.logic.model.Message
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class RemoteMessageDto(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val description: String? = null,
    val likes: Int,
    val date: Date? = null,
    val ownerId: String,
    val ownerName: String? = null
)

fun Message.toRemoteDto() =
    RemoteMessageDto(
        id = id,
        latitude = latitude,
        longitude = longitude,
        address = address,
        description = description,
        likes = likes,
        ownerId = ownerId
    )

fun RemoteMessageDto.toModel() = Message(
    id = id,
    latitude = latitude,
    longitude = longitude,
    address = address,
    description = description,
    likes = likes,
    date = date!!,
    ownerId = ownerId,
    ownerName = ownerName!!
)
