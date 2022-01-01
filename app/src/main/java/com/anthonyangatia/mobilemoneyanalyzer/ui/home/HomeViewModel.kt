package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.Prefs
import com.anthonyangatia.mobilemoneyanalyzer.database.Business
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.buildReceiptFromSms
import com.anthonyangatia.mobilemoneyanalyzer.util.getTempReceipts
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(val application: Application) : ViewModel() {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var business: LiveData<List<Business>> = database.getBusiness()
    var receipts:LiveData<List<Receipt>> = database.getAllReceipts()!!
    var persons:LiveData<List<Person>> = database.getPeople()
    private val prefs = Prefs(application)

    init {
//        processTempReceipts()
        prefs.newPhone = true //For debugging purpose
        if(prefs.newPhone){
            viewModelScope.launch {
                database.clear()
                database.clearPerson()
                database.clearBusiness()
                readSMS()
                prefs.newPhone = false
            }
        }else{
//            TODO: Check whether the last receipt in content provider is the same as the one in my database
//            If not, write a recursive algorithm that tries to establish the last message
        }
    }

    fun readSMS(){
        val uri = Telephony.Sms.Inbox.CONTENT_URI
        val projection = arrayOf("address", "body")
        val selectionClause = "address IN(?,?)"
        val selectionArgs:Array<String> = arrayOf("MPESA")// TODO: Add KCB in the array
        val cursor = application.contentResolver.query(uri, projection, selectionClause, selectionArgs, null)
        if (cursor != null) {
            val bodyIndex = cursor.getColumnIndexOrThrow("body")
            //Alternative solutuion
//            if (cursor.moveToFirst()) {
//                do {
//                    ...
//                } while (cursor.moveToNext());
//            }
            while (cursor.moveToNext()) {
                val (receipt, person, business) = buildReceiptFromSms(cursor.getString(bodyIndex))
                viewModelScope.launch {
                    if (receipt != null){
                        database.insert(receipt)
                        Timber.i("after inserted receipt: Loop"+cursor.position.toString())
                    }
                    if(person != null){
                        database.insertPerson(person)
                        Timber.i("after insert person: Loop"+cursor.position.toString())
                    }
                    if(business != null){
                        database.insertBusiness(business)
                        Timber.i("after insert business: Loop"+cursor.position.toString())
                    }
                }
                //TODO:Remove after proof of concept
                if(cursor.position > 1000) break
            }
        }else{
            Timber.i("Cursor is empty")
        }
        cursor?.close()
    }

    private fun processTempReceipts() {
        val messages =  getTempReceipts()
        for(message in messages){
            val (receipt, person, business) = buildReceiptFromSms(message)
            viewModelScope.launch {
                if (receipt != null) {
                    database.insert(receipt)
                }
                if (person != null) {
                    database.insertPerson(person)
                }
                if (business != null) {
                    database.insertBusiness(business)
                }
            }
        }
    }


}