package com.anthonyangatia.mobilemoneyanalyzer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NewDatabaseTest {
    private lateinit var receiptsDatabase: ReceiptsDatabase
    private lateinit var receiptsDao: ReceiptsDao

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        receiptsDatabase = Room.inMemoryDatabaseBuilder(context, ReceiptsDatabase::class.java).
                allowMainThreadQueries().build()
        receiptsDao = receiptsDatabase.receiptsDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        receiptsDatabase.close()
    }
}