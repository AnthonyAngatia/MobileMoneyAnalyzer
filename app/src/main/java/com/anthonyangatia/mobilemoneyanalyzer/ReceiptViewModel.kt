package com.anthonyangatia.mobilemoneyanalyzer

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
//            database.clear()
        }
        val prefs = Prefs(application)
        if(prefs.newPhone){
            viewModelScope.launch {
//                readSMS(application)
                prefs.newPhone = true
            }
        }else{
//            TODO: Check whether the last receipt in content provider is the same as the one in my database
//            If not, write a recursive algorithm that tries to establish the last message
        }


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
            var validMessages = ArrayList<String>()
            val invalidMessages = ArrayList<String>()
            cursor.moveToNext()
//            cursor.moveToPosition(2000)
            while (cursor.moveToNext()) {
                address.add(cursor.getString(addressIndex))
                body.add(cursor.getString(bodyIndex))
                if(checkRegex(cursor.getString(bodyIndex))){
                    validMessages.add(cursor.getString(bodyIndex))
                }else{
                    invalidMessages.add(cursor.getString(bodyIndex))
                }
                if(cursor.position > 100)
                    break

            }
        }else{
            Timber.i("Cursor is empty")
        }
        cursor?.close()
    }

     fun checkRegex(message:String):Boolean{
        val sentMoneyRegex = SENT_MONEY_REGEX_STRING.toRegex()
        val receiveMoneyRegex = RECEIVED_MONEY_REGEX_STRING.toRegex()
         val accountBalanceRegex = ACCOUNT_BALANCE.toRegex()
//         TODO: refactor the code to avoid duplicity
        if(sentMoneyRegex.matches(message) ){
            val matchResult = sentMoneyRegex.matchEntire(message)
            var (code, amountSent, paidSent,recipient, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            viewModelScope.launch {
//                TODO:1 Rename to sendTransaction
                database.insert(receipt = Receipt(0L,message, code, recipient, null, "sent", convertDateToLong(date+" "+time), time, convertToDouble(balance), convertToDouble(amountSent), null,convertToDouble(transactionCost)) )
            }

//            TODO:1. Insert into the database
            return true
        }else if(receiveMoneyRegex.matches(message)) {
            val matchResult = receiveMoneyRegex.matchEntire(message)
            var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
            time = formatTime(time)
            viewModelScope.launch {
//                TODO:2 Rename to receiveTransaction
                database.insert(receipt = Receipt(0L, message , code, null, sender, "received", convertDateToLong(date+" "+time), time, convertToDouble(balance), null, convertToDouble(amountReceived), null ) )
            }

            return true
        }
         when(true){
             sentMoneyRegex.matches(message)->{
                 val matchResult = sentMoneyRegex.matchEntire(message)
                 var (code, amountSent, paidSent,recipient, date, time, balance, transactionCost) = matchResult!!.destructured
                 time = formatTime(time)
                 viewModelScope.launch {
//TODO: Rename
                     database.insert(receipt = Receipt(0L,message, code, recipient, null, "sent", convertDateToLong(date+" "+time), time, convertToDouble(balance), convertToDouble(amountSent), null,convertToDouble(transactionCost)) )
                 }
                 return true
             }
             receiveMoneyRegex.matches(message)->{
                 val matchResult = receiveMoneyRegex.matchEntire(message)
                 var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
                 time = formatTime(time)
                 viewModelScope.launch {
//                     TODO: Rename and remember the bug in the receiver
                     database.insert(receipt = Receipt(0L, message , code, null, sender, "received", convertDateToLong(date+" "+time), time, convertToDouble(balance), null, convertToDouble(amountReceived), null ) )
                 }
                 return true
             }
             accountBalanceRegex.matches(message)->{
                 val matchResult = accountBalanceRegex.matchEntire(message)
                 var (code, mpesaBalance, businessBalance, date, time, transactionCost) = matchResult!!.destructured
                 time = formatTime(time)
                 viewModelScope.launch {
                     database.insert(receipt = Receipt(0L, message, code, null, null, "balance", convertDateToLong(date + " " + time), time, convertToDouble(mpesaBalance), null, null, convertToDouble(transactionCost)))
                 }
                 Timber.i("Receiver insert into the database")
             }

         }
        return false
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