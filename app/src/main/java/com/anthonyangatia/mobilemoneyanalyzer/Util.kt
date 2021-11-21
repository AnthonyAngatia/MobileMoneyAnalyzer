package com.anthonyangatia.mobilemoneyanalyzer

import androidx.room.ColumnInfo


data class AmountTransacted(
    @ColumnInfo(name = "totalSent")var amountSentTotal: Double?,
    @ColumnInfo(name = "totalReceived")var amountReceivedTotal:Double?
)

