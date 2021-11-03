package com.anthonyangatia.mobilemoneyanalyzer

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 1
    private val TAG = MainActivity::class.java.simpleName
    val CHANNEL_ID = "Test_Channel_ID"
    var messages: List<String> = ArrayList()
    private val messageTextView: TextView? = null
    private val columnNames: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForSmsPermission()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_SMS),
                PackageManager.PERMISSION_GRANTED
            )
        } else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }

    }
//TODO:Read on ContentProviders
    fun readSMS(view: View){
        val uri = Telephony.Sms.Inbox.CONTENT_URI
        val projection = arrayOf("address", "body")
        val selectionClause = "address IN(?,?)"
        val selectionArgs:Array<String> = arrayOf("KCB","MPESA")
        val cursor = contentResolver.query(uri, projection, selectionClause, selectionArgs, null)
        if (cursor != null) {
            val addressIndex = cursor.getColumnIndexOrThrow("address")
            val bodyIndex = cursor.getColumnIndexOrThrow("address")
            val address: MutableList<String> = ArrayList()//"Jumia, Safaricom, 0791278088"
            val body: MutableList<String> = ArrayList()//"The message itself"
            while (cursor.moveToNext()) {
                address.add(cursor.getString(addressIndex))
                body.add(cursor.getString(bodyIndex))
            }
            }else{
                Log.i("TAG", "Cursor is empty")
            }
        cursor?.close()
    }

    private fun checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, getString(R.string.permission_not_granted))
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_SMS),
                MY_PERMISSIONS_REQUEST_RECEIVE_SMS
            )
        } else {
            // Permission already granted. Enable the message button.
//            enableSmsButton();
//            Toast.makeText(this, "Permission Already granted", Toast.LENGTH_SHORT).show();
        }
    }

}