package com.anthonyangatia.mobilemoneyanalyzer

import android.app.Application
import android.content.ContentResolver
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import kotlinx.coroutines.launch
import java.util.ArrayList

class ReceiptViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {
    init {
        Log.i(javaClass.simpleName, "Created A view SmsReceiptViewModel" )
    }

    fun readSms(contentResolver: ContentResolver){
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
    fun insertReceipts(receipt: TransactionReceipt){
//        viewModelScope.launch {
            processText()
//        }
    }

    fun processText(){
        //InitializeReceipt
        var receipt = TransactionReceipt()
//        TODO: While loop
//        database.insert(receipt)
    }


}