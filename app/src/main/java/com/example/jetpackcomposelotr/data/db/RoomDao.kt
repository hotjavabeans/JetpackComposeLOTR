package com.example.jetpackcomposelotr.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jetpackcomposelotr.data.remote.responses.Character

@Dao
interface RoomDao {

    @Query("SELECT * FROM my_character_list")
    fun getAll(): List<RoomEntity>

    @Query("SELECT * from my_character_list where uid = :id")
    fun getById(id: Int) : RoomEntity?

    @Insert
    suspend fun insert(char: RoomEntity)

    @Update
    suspend fun update(char: RoomEntity)

    @Delete
    suspend fun delete(char: RoomEntity)

    @Query("DELETE FROM my_character_list")
    suspend fun deleteAllTodos()
}


/*@Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(character: Character)*/