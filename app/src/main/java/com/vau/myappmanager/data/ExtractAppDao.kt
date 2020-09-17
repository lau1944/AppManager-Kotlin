package com.vau.myappmanager.data

import androidx.room.*
import com.vau.myappmanager.objects.ExtractApp
import kotlinx.coroutines.flow.Flow

@Dao
interface ExtractAppDao {
    @Query("SELECT * FROM ExtractApp")
    fun getAll() : Flow<List<ExtractApp>>

    @Query("SELECT COUNT(*) FROM ExtractApp WHERE packageName = (:packageName)")
    fun isLiked(packageName: String): Flow<Int>

    @Query("Delete FROM ExtractApp")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg ExtractApp: ExtractApp)

    @Delete
    suspend fun delete(ExtractApp:ExtractApp)
}