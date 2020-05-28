package com.github.vasniktel.snooper.logic.message.local.feed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FeedDao {
    @Query("""
        select * from feed where feedOwnerId = :feedOwnerId
        order by date desc
    """)
    suspend fun getFeedOf(feedOwnerId: String): List<FeedDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feed: List<FeedDto>)

    @Query("delete from feed where feedOwnerId = :feedOwnerId")
    suspend fun deleteFeedOf(feedOwnerId: String)

    @Query("delete from feed")
    suspend fun deleteAll()
}
