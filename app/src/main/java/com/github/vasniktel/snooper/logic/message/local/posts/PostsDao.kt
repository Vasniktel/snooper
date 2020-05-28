package com.github.vasniktel.snooper.logic.message.local.posts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostsDao {
    @Query("""
        select * from posts where postOwnerId = :postOwnerId
        order by date desc
    """)
    suspend fun getPostsOf(postOwnerId: String): List<PostDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feed: List<PostDto>)

    @Query("delete from posts where postOwnerId = :postOwnerId")
    suspend fun deletePostsOf(postOwnerId: String)

    @Query("delete from posts")
    suspend fun deleteAll()
}
