package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt

@Dao
interface ReceiptsDao{
    @Insert
    fun insert(receipt: Receipt)
//     suspend fun insert(receipt: TransactionReceipt)

    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
    fun getReceipt(code:String): Receipt?
//     suspend fun getReceipt(code:String): TransactionReceipt?

    @Query("SELECT * from transaction_receipt_table")
    fun getAllReceipts(): List<Receipt>?

    @Query("DELETE FROM transaction_receipt_table")
    fun clear()
//TODO: Figure out why coroutines is not working:https://developer.android.com/codelabs/kotlin-android-training-coroutines-and-room#5


}