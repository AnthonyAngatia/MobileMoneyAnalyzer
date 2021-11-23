package com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.anthonyangatia.mobilemoneyanalyzer.RECEIVED_MONEY_REGEX_STRING
import com.anthonyangatia.mobilemoneyanalyzer.SENT_MONEY_REGEX_STRING
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat


class SmsReceiver : BroadcastReceiver() {
    private val SMS = "android.provider.Telephony.SMS_RECEIVED"

    //    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Timber.i("onReceive")
        val database = ReceiptsDatabase.getInstance(context).receiptsDao
        val sentMoneyRegex = SENT_MONEY_REGEX_STRING.toRegex()
        val receiveMoneyRegex = RECEIVED_MONEY_REGEX_STRING.toRegex()
        GlobalScope.launch {
            if (intent.action == SMS) {
                val bundle = intent.extras
                val pduses = bundle!!["pdus"] as Array<Any>?
                val messages = arrayOfNulls<SmsMessage>(pduses!!.size)
                for (i in pduses!!.indices) {
                    messages[i] = SmsMessage.createFromPdu(pduses!![i] as ByteArray)
                }
                val broadCastedMessage = messages[0].toString()
                if (sentMoneyRegex.matches(broadCastedMessage)) {
                    val matchResult = sentMoneyRegex.matchEntire(broadCastedMessage)
                    var (code, amountSent, paidSent, recipient, date, time, balance, transactionCost) = matchResult!!.destructured
                    time = formatTime(time)
                    Timber.i("Inserting sent message")
                    database.insert(receipt = Receipt(0L, broadCastedMessage, code, recipient, null, "sent", convertDateToLong(date + " " + time), time, convertToDouble(balance), convertToDouble(amountSent), null, convertToDouble(transactionCost)))
                } else if (receiveMoneyRegex.matches(broadCastedMessage)) {
                    val matchResult = receiveMoneyRegex.matchEntire(broadCastedMessage)
                    var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
                    time = formatTime(time)
                    Timber.i("Inserting received messages")
                    database.insert(receipt = Receipt(0L, broadCastedMessage, code, null, sender, "received", convertDateToLong(date + " " + time), time, convertToDouble(balance), null, convertToDouble(amountReceived), null))
                    Toast.makeText(context, messages[0]!!.messageBody, Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
    private fun formatTime(date: String): String {
        var newstring = date.replace("AM", "a.m.")
        if(newstring.equals(date)){
            var newstring = date.replace("PM", "p.m.")
            return newstring
        }
        return newstring
    }

    fun convertToDouble(value: String):Double{
        return value.replace(",", "").toDouble()
    }


    fun convertDateToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yy hh:mm aa")
        val date = dateFormat.parse(dateString)
        return date.time
    }
}



