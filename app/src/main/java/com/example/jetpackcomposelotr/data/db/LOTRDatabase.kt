package com.example.jetpackcomposelotr.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Character::class], version = 1)
abstract class LOTRDatabase: RoomDatabase() {
    abstract fun
}