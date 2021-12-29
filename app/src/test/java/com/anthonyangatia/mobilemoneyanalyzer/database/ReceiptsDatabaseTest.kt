package com.anthonyangatia.mobilemoneyanalyzer.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
class ReceiptsDatabaseTest {
    private lateinit var receiptsDatabase: ReceiptsDatabase
    private lateinit var receiptsDao: ReceiptsDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        receiptsDatabase = Room.inMemoryDatabaseBuilder(context, ReceiptsDatabase::class.java).
        allowMainThreadQueries().build()
        receiptsDao = receiptsDatabase.receiptsDao
    }
    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetPerson(){
        val person = Person("0791", "Anthony")
        receiptsDao.insertPerson(person)
        val personR =receiptsDao.getPerson("0791")
        assertEquals(personR.phoneNumber, person.phoneNumber)

    }

    @After
    fun tearDown() {
    }
}