package com.example.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities WHERE userId = :userId ORDER BY isCompleted ASC, createdAtEpoch DESC")
    fun observeActivities(userId: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE userId = :userId AND id = :id LIMIT 1")
    suspend fun getActivityById(userId: String, id: String): ActivityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(activity: ActivityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activities: List<ActivityEntity>)

    @Update
    suspend fun update(activity: ActivityEntity)

    @Delete
    suspend fun delete(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE id = :id AND userId = :userId")
    suspend fun deleteById(userId: String, id: String)

    @Query("SELECT * FROM activities WHERE userId = :userId AND isSynced = 0")
    suspend fun getUnsyncedActivities(userId: String): List<ActivityEntity>

    @Query("UPDATE activities SET isSynced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)
}
