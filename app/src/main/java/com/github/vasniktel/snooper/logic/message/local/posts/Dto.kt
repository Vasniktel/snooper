package com.github.vasniktel.snooper.logic.message.local.posts

import androidx.room.*
import com.github.vasniktel.snooper.logic.message.local.DateConverter
import com.github.vasniktel.snooper.logic.message.local.feed.FeedDto
import com.github.vasniktel.snooper.logic.model.Location
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import java.util.*

@Entity(
    tableName = "posts",
    indices = [
        Index(value = ["postOwnerId", "postId"], unique = true)
    ]
)
@TypeConverters(value = [DateConverter::class])
data class PostDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postOwnerId: String,
    val postId: Int,
    @Embedded
    val location: Location,
    @Embedded(prefix = "owner")
    val owner: User,
    val likes: Int,
    val description: String?,
    val date: Date
) {
    companion object {
        fun of(postOwnerId: String, message: Message) =
            PostDto(
                postOwnerId = postOwnerId,
                postId = message.id,
                location = message.location,
                owner = message.owner,
                likes = message.likes,
                description = message.description,
                date = message.date
            )
    }
}

fun PostDto.toModel() =
    Message(
        id = postId,
        location = location,
        owner = owner,
        likes = likes,
        description = description,
        date = date
    )
