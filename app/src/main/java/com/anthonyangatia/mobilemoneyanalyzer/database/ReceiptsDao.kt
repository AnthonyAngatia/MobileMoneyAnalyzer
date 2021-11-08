package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anthonyangatia.mobilemoneyanalyzer.TransactionReceipt

@Dao
interface ReceiptsDao{
    @Insert
    fun insert(receipt: TransactionReceipt)
//     suspend fun insert(receipt: TransactionReceipt)

    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
    fun getReceipt(code:String): TransactionReceipt?
//     suspend fun getReceipt(code:String): TransactionReceipt?
//TODO: Figure out why coroutines is not working:https://developer.android.com/codelabs/kotlin-android-training-coroutines-and-room#5


}