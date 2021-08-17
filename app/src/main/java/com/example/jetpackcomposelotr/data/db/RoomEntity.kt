package com.example.jetpackcomposelotr.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_character_list")
data class RoomEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "character_name") val characterName: String?,
    @ColumnInfo(name = "is_completed") var isDone: Boolean = false
)
