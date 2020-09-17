package com.vau.myappmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vau.myappmanager.objects.LikedApp
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedDao {
    @Query("SELECT * FROM LikedApp")
    fun getAll() : Flow<List<LikedApp>>

    @Query("SELECT COUNT(*) FROM LikedApp WHERE packageName = (:packageName)")
    fun isLiked(packageName: String): Flow<Int>

    @Insert
    suspend fun insert(vararg LikedApp: LikedApp)

    @Delete
    suspend fun delete(LikedApp: LikedApp)
}