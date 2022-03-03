package com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.anthonyangatia.mobilemoneyanalyzer.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.CREATEFROMPDUFORMAT
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat


class SmsReceiver : BroadcastReceiver() {
    private val PROCESSMESSAGE = "com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver.action.PROCESSMESSAGE"
    private val SMS = "android.provider.Telephony.SMS_RECEIVED"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Timber.i("onReceive")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(Intent(context, SmsService::class.java))
        } else {
            context.startService(Intent(context, SmsService::class.java))
        }
        if (intent.action == SMS) {
            val bundle = intent.extras
            val pduses = bundle!!["pdus"] as Array<Any>?
            val messages = arrayOfNulls<SmsMessage>(pduses!!.size)
            for (i in pduses!!.indices) {
                messages[i] = SmsMessage.createFromPdu(pduses!![i] as ByteArray, CREATEFROMPDUFORMAT)
                break
            }
            val broadCastedMessage = messages[0]!!.messageBody
            val intentService = Intent(context, MyIntentService::class.java)
            intentService.action = PROCESSMESSAGE
            intentService.putExtra("MESSAGE", broadCastedMessage)
            context.startService(intentService)
        }
    }
}



