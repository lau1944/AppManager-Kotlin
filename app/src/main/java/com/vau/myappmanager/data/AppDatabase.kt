package com.vau.myappmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vau.myappmanager.objects.LikedApp
import com.vau.myappmanager.objects.ExtractApp

@Database(entities = [LikedApp::class, ExtractApp::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun LikedDao(): LikedDao
    abstract fun extractAppDao() : ExtractAppDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "AppManagerDatabase")
                .build()
        }
    }
}