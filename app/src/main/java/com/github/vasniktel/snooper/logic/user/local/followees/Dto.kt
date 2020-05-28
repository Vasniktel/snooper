package com.github.vasniktel.snooper.logic.user.local.followees

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.vasniktel.snooper.logic.model.User

@Entity(
    tableName = "followees",
    indices = [
        Index(
            value = ["userId", "followerId"],
            unique = true
        )
    ]
)
data class FolloweesDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val followerId: String,
    val userId: String,
    val name: String,
    val photoUrl: String?
) {
    companion object {
        fun of(followerId: String, followee: User) =
            FolloweesDto(
                id = 0,
                followerId = followerId,
                userId = followee.id,
                name = followee.name,
                photoUrl = followee.photoUrl
            )
    }
}

fun FolloweesDto.toModel() =
    User(
        id = userId,
        name = name,
        photoUrl = photoUrl
    )

