package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt

@Dao
interface ReceiptsDao{
    @Insert
    suspend fun insert(receipt: Receipt)

    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
    suspend fun getReceipt(code:String): Receipt?

    @Query("SELECT * from transaction_receipt_table")
    fun getAllReceipts(): LiveData<List<Receipt>>?

    @Query("SELECT * from transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate")
    fun getReceiptWhereDate(beginningDate:Long , endDate:Long ): LiveData<List<Receipt>>?

    @Query("DELETE FROM transaction_receipt_table")
    suspend fun clear()
//TODO:(cleared) Figure out why coroutines is not working:https://developer.android.com/codelabs/kotlin-android-training-coroutines-and-room#5


}