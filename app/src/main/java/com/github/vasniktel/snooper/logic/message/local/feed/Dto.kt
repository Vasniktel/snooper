package com.github.vasniktel.snooper.logic.message.local.feed

import android.accounts.AuthenticatorDescription
import androidx.room.*
import com.github.vasniktel.snooper.logic.message.local.DateConverter
import com.github.vasniktel.snooper.logic.model.Location
import com.github.vasniktel.snooper.logic.model.Message
import com.github.vasniktel.snooper.logic.model.User
import java.util.*

@Entity(
    tableName = "feed",
    indices = [
        Index(value = ["feedOwnerId", "postId"], unique = true)
    ]
)
@TypeConverters(value = [DateConverter::class])
data class FeedDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val feedOwnerId: String,
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
        fun of(feedOwnerId: String, message: Message) =
            FeedDto(
                feedOwnerId = feedOwnerId,
                postId = message.id,
                location = message.location,
                owner = message.owner,
                likes = message.likes,
                description = message.description,
                date = message.date
            )
    }
}

fun FeedDto.toModel() =
    Message(
        id = postId,
        location = location,
        owner = owner,
        likes = likes,
        description = description,
        date = date
    )
