package com.github.vasniktel.snooper.logic.user.local.followees

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FolloweesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<FolloweesDto>)

    @Query("delete from followees where followerId = :followerId")
    suspend fun deleteFolloweesOf(followerId: String)

    @Query("delete from followees")
    suspend fun deleteAll()

    @Query("select * from followees where followerId = :followerId")
    suspend fun getFolloweesOf(followerId: String): List<FolloweesDto>
}