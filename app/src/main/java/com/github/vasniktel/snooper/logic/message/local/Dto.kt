package com.github.vasniktel.snooper.logic.message.local

/*import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.vasniktel.snooper.logic.model.LocationMessage
import java.util.*

@Entity(tableName = "messages")
data class LocalMessageDto(
    @PrimaryKey
    @ColumnInfo val id: Int,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val ownerId: String,
    @ColumnInfo val ownerName: String,
    @ColumnInfo val likes: Int,
    @ColumnInfo val address: String?,
    @ColumnInfo val description: String?,
    @ColumnInfo val date: Long
)

fun LocalMessageDto.toModel() = LocationMessage(
    id = id,
    latitude = latitude,
    longitude = longitude,
    ownerName = ownerName,
    ownerId = ownerId,
    likes = likes,
    address = address,
    description = description,
    date = Date(date)
)

fun LocationMessage.toLocalDto() = LocalMessageDto(
    id = id,
    latitude = latitude,
    longitude = longitude,
    ownerName = ownerName,
    ownerId = ownerId,
    likes = likes,
    address = address,
    description = description,
    date = date.time
)*/
