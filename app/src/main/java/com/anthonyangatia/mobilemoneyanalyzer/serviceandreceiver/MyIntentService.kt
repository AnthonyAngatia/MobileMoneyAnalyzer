package com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.anthonyangatia.mobilemoneyanalyzer.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
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
                handleActionProcessMessage(param1)
//                getFromDatabase()
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
    private fun handleActionProcessMessage(param: String?) {
        val database = ReceiptsDatabase.getInstance(applicationContext).receiptsDao
        val sentMoneyRegex = SENT_MONEY_REGEX_STRING.toRegex()
        val receiveMoneyRegex = RECEIVED_MONEY_REGEX_STRING.toRegex()
        val accountBalanceRegex = ACCOUNT_BALANCE.toRegex()
        if (param != null){
            when(true){
                sentMoneyRegex.matches(param) ->{
                    val matchResult = sentMoneyRegex.matchEntire(param)
                    var (code, amountSent, paidSent, recipient, date, time, balance, transactionCost) = matchResult!!.destructured
                    time = formatTime(time)
                    Timber.i("Inserting sent message")
                    GlobalScope.launch {
                        database.insert(receipt = Receipt(0L, param, code, recipient, null, "sent", convertDateToLong(date + " " + time), time, convertToDouble(balance), convertToDouble(amountSent), null, convertToDouble(transactionCost)))
                    }

                }
                receiveMoneyRegex.matches(param)->{
                    val matchResult = receiveMoneyRegex.matchEntire(param)
                    var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
                    time = formatTime(time)
                    Timber.i("Inserting received messages")
                    GlobalScope.launch {
                        database.insert(receipt = Receipt(0L, param, code, null, sender, "received", convertDateToLong(date + " " + time), time, convertToDouble(balance), null, convertToDouble(amountReceived), null))
                    }

//                        Toast.makeText(context, messages[0]!!.messageBody, Toast.LENGTH_SHORT).show()
                }
                accountBalanceRegex.matches(param)->{
                    val matchResult = accountBalanceRegex.matchEntire(param)
                    var (code, mpesaBalance, businessBalance, date, time, transactionCost) = matchResult!!.destructured
                    time = formatTime(time)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO){
                            val receipt = Receipt(0L, param, code,
                                null, null, "balance", convertDateToLong(date + " " + time), time,
                                convertToDouble(mpesaBalance), null, null, convertToDouble(transactionCost))
                            //Fire a notification to classify the transaction
                            fireNotification(receipt)

                            val id = database.insert(receipt)
                            Timber.i("Id is"+id.toString())

                            val rec = database.getReceipt(68895)
                            Timber.i(rec.toString())
//                            Timber.i(receipt.toString())
                            val rec2 = database.getLastReceipt2()
                            Timber.i(rec2.toString())

//                            Timber.i("Receiver insert into the database")

                        }

                    }

                }


            }
        }
    }

    private fun insertToSharedPreferences(receipt: Receipt) {
        val prefs = Prefs(applicationContext)
        val gson = Gson()
        val receiptString = gson.toJson(receipt)
        //TODO:[VERY IMPORTANT] The below code puts only one receipt, we need to find a solution for putting multiple receipts
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