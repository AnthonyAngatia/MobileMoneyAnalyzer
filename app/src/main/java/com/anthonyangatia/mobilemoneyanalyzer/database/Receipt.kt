package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction_receipt_table")
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    var receiptId: Long = 0L,

    @ColumnInfo(name = "receipt_code")
    var code: String = "default-code",

    @ColumnInfo(name = "recipient")
    var recipient: String? = "default-recipient",

    @ColumnInfo(name = "sender")
    var sender: String? = "default-sender",

    @ColumnInfo(name = "transactionType")
    var transactionType: String? = null,

    var date: String? = null,//TODO: Look for valid type for dates and time

    var time: String? = null,

    @ColumnInfo(name = "account_balance")
    var balance: String = "0.0",

    @ColumnInfo(name = "amount_sent")
    var amountSent: String? = "10.0",

    @ColumnInfo(name = "amount_received")
    var amountReceived: String? = "0.0",

    @ColumnInfo(name="transaction_cost")
    var transactionCost: String? ="0.0")