package com.github.vasniktel.snooper.logic.user.local.followers

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.vasniktel.snooper.logic.model.User

@Entity(
    tableName = "followers",
    indices = [
        Index(
            value = ["userId", "followeeId"],
            unique = true
        )
    ]
)
data class FollowersDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val followeeId: String,
    val userId: String,
    val name: String,
    val photoUrl: String?
) {
    companion object {
        fun of(followeeId: String, follower: User) =
            FollowersDto(
                id = 0,
                followeeId = followeeId,
                userId = follower.id,
                name = follower.name,
                photoUrl = follower.photoUrl
            )
    }
}

fun FollowersDto.toModel() =
    User(
        id = userId,
        name = name,
        photoUrl = photoUrl
    )
