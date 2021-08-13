package com.example.jetpackcomposelotr.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class], version = 1, exportSchema = false)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun roomDao(): RoomDao
    companion object {
        private var INSTANCE: com.example.jetpackcomposelotr.data.db.RoomDatabase? = null
        fun getInstance(context: Context): com.example.jetpackcomposelotr.data.db.RoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        com.example.jetpackcomposelotr.data.db.RoomDatabase::class.java,
                        "room_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}