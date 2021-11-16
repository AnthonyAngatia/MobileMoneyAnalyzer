package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "transaction_receipt_table")
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    var receiptId: Long = 0L,

    @ColumnInfo(name = "receipt_message")
    var message: String = "default-message",

    @ColumnInfo(name = "receipt_code")
    var code: String = "default-code",

    @ColumnInfo(name = "recipient")
    var recipient: String? = "default-recipient",

    @ColumnInfo(name = "sender")
    var sender: String? = "default-sender",

    @ColumnInfo(name = "transactionType")
    var transactionType: String? = null,

    var date: Long? = null,//TODO: Look for valid type for dates and time

    var time: String? = null,

    @ColumnInfo(name = "account_balance")
    var balance: Double? = null,

    @ColumnInfo(name = "amount_sent")
    var amountSent: Double? = null,

    @ColumnInfo(name = "amount_received")
    var amountReceived: Double? = null,

    @ColumnInfo(name="transaction_cost")
    var transactionCost: Double? =null)

