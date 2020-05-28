package com.github.vasniktel.snooper.logic.user.local.followers

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FollowersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<FollowersDto>)

    @Query("delete from followers where followeeId = :followeeId")
    suspend fun deleteFollowersOf(followeeId: String)

    @Query("delete from followers")
    suspend fun deleteAll()

    @Query("select * from followers where followeeId = :followeeId")
    suspend fun getFollowersOf(followeeId: String): List<FollowersDto>
}
