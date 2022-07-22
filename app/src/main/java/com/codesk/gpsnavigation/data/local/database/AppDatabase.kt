package com.codesk.gpsnavigation.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codesk.gpsnavigation.model.datamodels.SavedMapTable
import com.codesk.gpsnavigation.utill.commons.AppConstants.Companion.APP_DATABASE
import com.codesk.gpsnavigation.data.local.dao.SavedMapDao
import com.codesk.gpsnavigation.data.local.dao.SavedRecentMapDao
import com.codesk.gpsnavigation.model.datamodels.SavedRecentMapTable

@Database(
    entities = [SavedMapTable::class,SavedRecentMapTable::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedMapDao(): SavedMapDao
    abstract fun savedRecentMapDao(): SavedRecentMapDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATABASE
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}