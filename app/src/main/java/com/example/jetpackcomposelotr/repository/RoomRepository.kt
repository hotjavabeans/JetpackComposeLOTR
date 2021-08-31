package com.example.jetpackcomposelotr.repository

import androidx.lifecycle.LiveData
import com.example.jetpackcomposelotr.data.db.RoomDao
import com.example.jetpackcomposelotr.data.db.RoomEntity

class RoomRepository(
    private val roomDao: RoomDao
) {
    val readAll: List<RoomEntity> = roomDao.getAll()

    suspend fun addTodo(char: RoomEntity) {
        roomDao.insert(char)
    }
    suspend fun updateTodo(char: RoomEntity) {
        roomDao.update(char)
    }
    suspend fun deleteTodo(char: RoomEntity) {
        roomDao.delete(char)
    }
    suspend fun deleteAllTodos() {
        roomDao.deleteAllTodos()
    }
}