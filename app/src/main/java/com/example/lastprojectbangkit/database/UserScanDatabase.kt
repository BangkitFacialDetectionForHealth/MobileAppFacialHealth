package com.example.lastprojectbangkit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lastprojectbangkit.data.model.StoryModel

@Database(
    entities =
    [StoryModel::class, RemoteKeys::class],
    version = 1,
    exportSchema = false)
abstract class UserScanDatabase : RoomDatabase() {

    abstract fun userStoryDao(): UserStoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: UserScanDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserScanDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserScanDatabase::class.java, "user_story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}