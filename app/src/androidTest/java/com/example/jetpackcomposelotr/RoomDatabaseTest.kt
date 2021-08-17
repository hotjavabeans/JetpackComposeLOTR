package com.example.jetpackcomposelotr

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.jetpackcomposelotr.data.db.RoomDao
import com.example.jetpackcomposelotr.data.db.RoomDatabase
import com.example.jetpackcomposelotr.data.db.RoomEntity
import org.junit.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    private lateinit var roomDao: RoomDao
    private lateinit var db: RoomDatabase

    @Before
    fun createDb() {
        /*val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, RoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()*/

        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            RoomDatabase::class.java
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()

        roomDao = db.roomDao()
    }

    @After
    @Throws(IOException::class)
    fun deleteDb() {
        if (::db.isInitialized)
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetChar() = runBlocking {
        val char = RoomEntity(uid = 1, characterName = "Dummy Character", isDone = false)
        roomDao.insert(char)
        val oneChar = roomDao.getById(1)
        assertEquals(oneChar?.uid, 1)
    }
}