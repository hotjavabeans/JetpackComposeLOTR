package com.example.jetpackcomposelotr.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.jetpackcomposelotr.data.remote.responses.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(character: Character)


}