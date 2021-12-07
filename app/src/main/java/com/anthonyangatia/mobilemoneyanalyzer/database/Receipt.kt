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
    var code: String? = null,

    @ColumnInfo(name = "recipient_name")
    var recipientName: String? = "default-recipient",

    @ColumnInfo(name = "recipient_number")
    var recipientNumber: Long? = null,

    @ColumnInfo(name = "account_number")
    var account: String? = null,

    @ColumnInfo(name = "sender")
    var sender: String? = "default-sender",

    @ColumnInfo(name = "transactionType")
    var transactionType: String? = null,

    var date: Long? = null,//TODO: Look for valid type for dates and time

    var time: String? = null,

    @ColumnInfo(name = "account_balance")
    var balance: Double? = null,

    @ColumnInfo(name = "amount_sent")
    var amountSent: Double? = 0.0,

    @ColumnInfo(name = "amount_received")
    var amountReceived: Double? = 0.0,

    @ColumnInfo(name="transaction_cost")
    var transactionCost: Double? =0.0)

