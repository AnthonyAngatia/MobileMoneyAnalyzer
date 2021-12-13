package com.anthonyangatia.mobilemoneyanalyzer.ui.search

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.buildReceiptFromSms
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList

class SearchViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {
    var receipts: LiveData<List<Receipt>>

    init {
        readSMS(application)
        receipts = database.getAllReceipts()!!
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