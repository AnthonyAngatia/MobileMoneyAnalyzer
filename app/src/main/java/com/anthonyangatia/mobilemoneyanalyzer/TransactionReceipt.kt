package com.anthonyangatia.mobilemoneyanalyzer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction_receipt_table")
data class TransactionReceipt(
    @PrimaryKey(autoGenerate = true)
    var receiptId: Long = 0L,
    @ColumnInfo(name = "receipt_code")
    var code: String = "default-code",
    @ColumnInfo(name = "recipient")
    var recipient: String? = "default-recipient" ,

    var date: Long? = System.currentTimeMillis() ,
    var time: Long? = System.currentTimeMillis(),
    @ColumnInfo(name = "account_balance")
    var balance: Double? = 0.0,
    @ColumnInfo(name = "amount_sent")
    var amountSent: Double? = 0.0,
    @ColumnInfo(name = "amount_received")
    var amountReceived: Double? = 0.0)