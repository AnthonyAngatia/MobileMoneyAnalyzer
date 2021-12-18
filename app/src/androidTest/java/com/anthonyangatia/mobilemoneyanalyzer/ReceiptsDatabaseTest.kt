package com.anthonyangatia.mobilemoneyanalyzer

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReceiptsDatabaseTest {
    private lateinit var receiptsDatabase: ReceiptsDatabase
    private lateinit var receiptsDao: ReceiptsDao
    @Before()
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        receiptsDatabase = Room.inMemoryDatabaseBuilder(context, ReceiptsDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        receiptsDao = receiptsDatabase.receiptsDao
    }

    @After
    @Throws(Exception::class)
    fun closeDb(){
    receiptsDatabase.close()
    }


    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetReceipts(){
        val receipt = Receipt(code="default-code",message = "Message")
        receiptsDao.insert(receipt)
        val transactionReceipt =receiptsDao.getReceipt("default-code")
        assertEquals(transactionReceipt?.code, "default-code")

    }



}