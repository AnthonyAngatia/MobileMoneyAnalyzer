package com.anthonyangatia.mobilemoneyanalyzer

import androidx.room.ColumnInfo


data class AmountTransacted(
    @ColumnInfo(name = "totalSent")var amountSentTotal: Double?,
    @ColumnInfo(name = "totalReceived")var amountReceivedTotal:Double?
)

val SMS_RECEIVE_ACTION = "android.provider.Telephony.SMS_RECEIVED"
val SENT_MONEY_REGEX_STRING = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) (paid|sent) to (?<recipient>.+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
val RECEIVED_MONEY_REGEX_STRING = """"${'"'}(?<code>\w+) Confirmed\.*\s*You have received Ksh(?<amountReceived>[\d\.\,]+) from (?<sender>.*) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\..*"""


