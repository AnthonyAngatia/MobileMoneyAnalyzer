package com.anthonyangatia.mobilemoneyanalyzer.ui.search

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.util.buildReceiptFromSms
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
            var y = 0L
            while (cursor.moveToNext()) {
                address.add(cursor.getString(addressIndex))
                body.add(cursor.getString(bodyIndex))
                val (receipt, person, business) = buildReceiptFromSms(cursor.getString(bodyIndex))
                if (receipt != null){
                    viewModelScope.launch {
                         y=  database.insert(receipt)

                    }
                }
                if(person != null){
                    viewModelScope.launch {
                        val x = database.insertPerson(person)
                        x
                    }
                }
                if(business != null){
                    viewModelScope.launch {
                        val x = database.insertBusiness(business)
                        x
                    }
                }
                //TODO:80REmove after proof of concept
                if(cursor.position > 50)
                    break
            }
        }else{
            Timber.i("Cursor is empty")
        }
        cursor?.close()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Receipt>> {
        return database.searchReceipt(searchQuery).asLiveData()
    }

}