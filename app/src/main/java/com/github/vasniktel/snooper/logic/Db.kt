package com.github.vasniktel.snooper.logic

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.vasniktel.snooper.logic.message.local.feed.FeedDao
import com.github.vasniktel.snooper.logic.message.local.feed.FeedDto
import com.github.vasniktel.snooper.logic.message.local.posts.PostDto
import com.github.vasniktel.snooper.logic.message.local.posts.PostsDao
import com.github.vasniktel.snooper.logic.user.local.followees.FolloweesDao
import com.github.vasniktel.snooper.logic.user.local.followees.FolloweesDto
import com.github.vasniktel.snooper.logic.user.local.followers.FollowersDao
import com.github.vasniktel.snooper.logic.user.local.followers.FollowersDto

@Database(
    entities = [
        FollowersDto::class,
        FolloweesDto::class,
        FeedDto::class,
        PostDto::class
    ],
    version = 5
)
abstract class Db : RoomDatabase() {
    abstract fun getFollowersDao(): FollowersDao
    abstract fun getFolloweesDao(): FolloweesDao
    abstract fun getFeedDao(): FeedDao
    abstract fun getPostsDao(): PostsDao
}
