package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import java.util.ArrayList

class HomeViewModel(application: Application) : ViewModel() {
    var business: LiveData<List<Business>>
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var receipts:LiveData<List<Receipt>>
    var persons:LiveData<List<Person>>

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    init {
//        processTempReceipts()

        val prefs = Prefs(application)
        prefs.newPhone = true //For debugging purpose
        if(prefs.newPhone){
            viewModelScope.launch {
                database.clear()
                database.clearPerson()
                database.clearBusiness()
                readSMS(application)
                prefs.newPhone = false
            }
        }else{
////            TODO: Check whether the last receipt in content provider is the same as the one in my database
////            If not, write a recursive algorithm that tries to establish the last message
        }


        receipts = database.getAllReceipts()!!
//        _lastReceipt.value = database.getLastReceipt()!!
        persons = database.getPeople()
        business = database.getBusiness()

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
                if(cursor.position > 100)
                    break
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