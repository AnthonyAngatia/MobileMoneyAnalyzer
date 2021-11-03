package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anthonyangatia.mobilemoneyanalyzer.TransactionReceipt

@Dao
interface ReceiptsDao{
    @Insert
    fun insert(receipt: TransactionReceipt)

    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
    fun getReceipt(code:String): TransactionReceipt?



}