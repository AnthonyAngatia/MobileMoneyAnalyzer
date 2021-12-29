package com.anthonyangatia.mobilemoneyanalyzer.serviceandreceiver

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.anthonyangatia.mobilemoneyanalyzer.util.SMS_RECEIVE_ACTION
import timber.log.Timber

class SmsService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Retu tion channel to the service.")
    }

    override fun onCreate() {
//        super.onCreate()
        //This process can be done from the manifest file as below but we are doing it here to pesist the thing
//        <receiver
//        android:name=".SMSReceiver"
//        android:enabled="true"
//        android:exported="true">
//        <intent-filter>
//        <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
//        </intent-filter>
//        </receiver>
        Timber.i("onCreate Service")
        initializeReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
         super.onStartCommand(intent, flags, startId)
        Timber.i("onStartCommand")
        return START_STICKY //https://developer.android.com/guide/components/services#ExtendingService
    }

    override fun onDestroy() {
        super.onDestroy()
//        initializeReceiver()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, SmsReceiver::class.java)
        this.sendBroadcast(broadcastIntent)

    }

    private fun initializeReceiver() {
        var smsReceiver = SmsReceiver()
        var intentFilter = IntentFilter().apply {
            this.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
            this.addAction(SMS_RECEIVE_ACTION)
        }
        this.registerReceiver(smsReceiver, intentFilter)
    }
}