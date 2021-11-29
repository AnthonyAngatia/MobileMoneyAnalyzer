package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anthonyangatia.mobilemoneyanalyzer.AmountTransacted

@Dao
interface ReceiptsDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receipt: Receipt):Long

    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
    suspend fun getReceipt(code:String): Receipt?

    @Query("SELECT * from transaction_receipt_table WHERE receiptId = :id ")
    suspend fun getReceipt(id:Long): Receipt?

    @Query("SELECT * from transaction_receipt_table")
    fun getAllReceipts(): LiveData<List<Receipt>>?

    @Query("SELECT * from transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate")
    fun getReceiptWhereDate(beginningDate:Long , endDate:Long ): LiveData<List<Receipt>>?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived from transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate")
    suspend fun getAmountTransactedList(beginningDate:Long , endDate:Long ): AmountTransacted?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived FROM transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate AND transactionType = :transactionType")
    suspend fun getSumOfTransactionType(beginningDate:Long , endDate:Long, transactionType:String):AmountTransacted?

    @Query("SELECT * FROM transaction_receipt_table LIMIT 1")
    fun getLastReceipt():LiveData<Receipt>?

    @Query("SELECT * FROM transaction_receipt_table LIMIT 1")
    fun getLastReceipt2():Receipt?

    @Query("DELETE FROM transaction_receipt_table")
    suspend fun clear()
//TODO:(cleared) Figure out why coroutines is not working:https://developer.android.com/codelabs/kotlin-android-training-coroutines-and-room#5


}