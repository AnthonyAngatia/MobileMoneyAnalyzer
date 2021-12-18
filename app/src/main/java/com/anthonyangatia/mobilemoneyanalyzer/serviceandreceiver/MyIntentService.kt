package com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.buildReceiptFromSms
import com.google.gson.Gson
import kotlinx.coroutines.*
import timber.log.Timber
import java.text.SimpleDateFormat

private const val PROCESSMESSAGE = "com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver.action.PROCESSMESSAGE"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.

 */
class MyIntentService : IntentService("MyIntentService") {

    val CHANNEL_ID = "Test_Channel_ID"

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            PROCESSMESSAGE -> {
                val param1 = intent.getStringExtra("MESSAGE")
                if (param1 != null) {
                    handleActionProcessMessage(param1)
                }
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun getFromDatabase(){
        val database = ReceiptsDatabase.getInstance(applicationContext).receiptsDao
        val receipt = database.getAllReceipts()!!
        val size = receipt.value?.size
        Timber.i("Rerurn"+ receipt.value.toString())

        GlobalScope.launch {
            val amt = database.getAmountTransactedList(1637830380000,1637985120000)
            Timber.i("Amount"+amt)
        }

    }
    private fun handleActionProcessMessage(param: String) {
        val database = ReceiptsDatabase.getInstance(applicationContext).receiptsDao

        val (receipt, person, business) = buildReceiptFromSms(param)
        if (receipt != null){
            GlobalScope.launch {
                database.insert(receipt)
            }
        }
        if(person != null){
            GlobalScope.launch {
                database.insertPerson(person)
            }
        }
        if(business != null){
            GlobalScope.launch {
                database.insertBusiness(business)
            }
        }
    }

    private fun insertToSharedPreferences(receipt: Receipt) {
        val prefs = Prefs(applicationContext)
        val gson = Gson()
        val receiptString = gson.toJson(receipt)
        if (prefs.index == -1) {
            prefs.objectPref = receiptString
            prefs.index++
        } else {
            prefs.objectPref = receiptString
            prefs.index++
        }
    }

    private fun fireNotification(receipt: Receipt) {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_account_balance_wallet_24)
                .setContentTitle("My notification")
                .setContentText(receipt.message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "Much longer text that cannot fit one line..." + receipt.code
                        )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Channel Name"
            val description = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(this.CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}