package com.volumecontrol.volumepanel.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.volumecontrol.volumepanel.datasource.local.dao.StylePanelDao
import com.volumecontrol.volumepanel.model.StylePanelTable
import com.volumecontrol.volumepanel.utills.commons.AppConstants.Companion.APP_DATABASE


@Database(
    entities = [StylePanelTable::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stylePanelDao(): StylePanelDao

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
                )
                    .fallbackToDestructiveMigration()
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