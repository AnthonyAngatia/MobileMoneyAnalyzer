package com.anthonyangatia.mobilemoneyanalyzer

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ReceiptViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {
    var receipts:LiveData<List<Receipt>>

//    private val _lastReceipt:MutableLiveData<Receipt> = MutableLiveData()

    var lastReceipt:LiveData<Receipt>?
//        get() = _lastReceipt

    init {
        viewModelScope.launch {
            database.clear()
        }
//        val prefs = Prefs(application)
//        if(prefs.newPhone){
//            viewModelScope.launch {
                readSMS(application)
//                prefs.newPhone = true
//            }
//        }else{
////            TODO: Check whether the last receipt in content provider is the same as the one in my database
////            If not, write a recursive algorithm that tries to establish the last message
//        }


        receipts = database.getAllReceipts()!!
//        _lastReceipt.value = database.getLastReceipt()!!
        lastReceipt = database.getLastReceipt()

    }
    fun getSms(){
        Timber.i("getSms called")
        receipts = database.getAllReceipts()!!
        Timber.i("Size"+receipts.value?.size)
    }



    //TODO 100:Read on ContentProviders
    fun readSMS(application: Application){
        val uri = Telephony.Sms.Inbox.CONTENT_URI
        val projection = arrayOf("address", "body")
        val selectionClause = "address IN(?,?)"
        val selectionArgs:Array<String> = arrayOf("MPESA")// TODO: Add KCB in the array
        val cursor = application.contentResolver.query(uri, projection, selectionClause, selectionArgs, null)
        if (cursor != null) {
            val addressIndex = cursor.getColumnIndexOrThrow("address")
            val bodyIndex = cursor.getColumnIndexOrThrow("body")
            val address: MutableList<String> = ArrayList()//"Jumia, Safaricom, 0791278088"
            val body: MutableList<String> = ArrayList()//"The message itself"
            val invalidMessages = ArrayList<String>()
            cursor.moveToNext()
//            cursor.moveToPosition(2000)
            while (cursor.moveToNext()) {
                address.add(cursor.getString(addressIndex))
                body.add(cursor.getString(bodyIndex))
                val receipt = buildReceiptFromSms(cursor.getString(bodyIndex))
                if(receipt.code == null){
                    invalidMessages.add(cursor.getString(bodyIndex))//Reporting unknown messages
                }
                //Insert into database
                viewModelScope.launch {
                    database.insert(receipt)
                }
                if(cursor.position > 300)
                    break
            }
        }else{
            Timber.i("Cursor is empty")
        }
        cursor?.close()
    }
}