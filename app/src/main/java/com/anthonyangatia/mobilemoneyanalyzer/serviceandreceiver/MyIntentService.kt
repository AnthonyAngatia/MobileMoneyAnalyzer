package com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.anthonyangatia.mobilemoneyanalyzer.MainActivity2
import com.anthonyangatia.mobilemoneyanalyzer.Prefs
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.KEY_TEXT_REPLY
import com.anthonyangatia.mobilemoneyanalyzer.util.buildReceiptFromSms
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

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
        createNotificationChannel()
        when (intent?.action) {
            PROCESSMESSAGE -> {
                val param1 = intent.getStringExtra("MESSAGE")
                if (param1 != null) {
                    Timber.i("My Intent Service has started")
                    handleActionProcessMessage(param1)
                }
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionProcessMessage(param: String) {
        val database = ReceiptsDatabase.getInstance(applicationContext).receiptsDao
        val (receipt, person) = buildReceiptFromSms(param)

        if (receipt != null) {
            when(receipt.transactionType){
                "sentToNumber", "sentBuyGoods", "sentToPayBill", "sentToMshwari" -> {
                    Timber.i("Found the correct transaction type")
                    Timer().schedule(2000) {
                        Timber.i("Timer schedule executed")
                        fireNotification(receipt)                    }
                }
                else -> Timber.i("Incorrect transacction type")
            }
            insertReceipt(database, receipt)

        }
        if(person != null){
            insertPerson(database, person)
        }
    }

    private fun insertPerson(
        database: ReceiptsDao,
        person: PersonAndBusiness?
    ) {
        GlobalScope.launch {
            Timber.i("Inserting person to the databse")
            if (person != null) {
                database.insertPerson(person)
            }
        }
    }

    private fun insertReceipt(
        database: ReceiptsDao,
        receipt: Receipt?
    ) {
        GlobalScope.launch {
            Timber.i("Inserting receipt to the database")
            if (receipt != null) {
                database.insert(receipt)
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

        var replyLabel: String = "REPLY"
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity2::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Build a PendingIntent for the reply action to trigger.
        var replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        var action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(R.drawable.ic_baseline_send_24,
                getString(R.string.classify).uppercase(), replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_account_balance_wallet_24)
                .setContentTitle("Transaction Classification")
                .setContentText("Classify your transaction!")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "Classify Expense Kshs "+receipt.amountSent + "sent to "+ receipt.name + "\n" + receipt.message
                        )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_baseline_account_balance_wallet_24,"FRIEND",pendingIntent)
                .addAction(action)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, builder.build())
        Timber.i("Building the notifications")
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

}