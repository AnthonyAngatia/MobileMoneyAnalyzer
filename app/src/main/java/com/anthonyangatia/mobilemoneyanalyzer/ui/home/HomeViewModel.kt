package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.Prefs
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.buildReceiptFromSms
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList

class HomeViewModel(application: Application) : ViewModel() {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var receipts:LiveData<List<Receipt>>

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    init {

        val prefs = Prefs(application)
        prefs.newPhone = false
        if(prefs.newPhone){
            viewModelScope.launch {
                database.clear()
                readSMS(application)
                prefs.newPhone = true
            }
        }else{
////            TODO: Check whether the last receipt in content provider is the same as the one in my database
////            If not, write a recursive algorithm that tries to establish the last message
        }


        receipts = database.getAllReceipts()!!
//        _lastReceipt.value = database.getLastReceipt()!!

    }

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
                val (receipt, person, business) = buildReceiptFromSms(cursor.getString(bodyIndex))
                if (receipt != null){
                    viewModelScope.launch {
                        database.insert(receipt)
                    }
                }
                if(person != null){
                    viewModelScope.launch {
                        database.insertPerson(person)
                    }
                }
                if(business != null){
                    viewModelScope.launch {
                        database.insertBusiness(business)
                    }
                }
                //TODO:80REmove after proof of concept
                if(cursor.position > 300)
                    break
            }
        }else{
            Timber.i("Cursor is empty")
        }
        cursor?.close()
    }

}