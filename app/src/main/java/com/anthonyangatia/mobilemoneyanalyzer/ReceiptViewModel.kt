package com.anthonyangatia.mobilemoneyanalyzer

import android.app.Application
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReceiptViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {
    var receipts =database.getAllReceipts()
    init {
        viewModelScope.launch {
            database.clear()
        }

        Log.i(javaClass.simpleName, "Created A view SmsReceiptViewModel" )
        viewModelScope.launch {
            readSMS(application)
        }
        viewModelScope.launch {
            receipts = database.getAllReceipts()!!
        }

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
                if(cursor.position > 500)
                    break

            }
        }else{
            Log.i("TAG", "Cursor is empty")
        }
        cursor?.close()
    }

     fun checkRegex(message:String):Boolean{
        val sentMoneyRegex = SENT_MONEY_REGEX_STRING.toRegex()
        val receiveMoneyRegex = RECEIVED_MONEY_REGEX_STRING.toRegex()
//         TODO: refactor the code to avoid duplicity
        if(sentMoneyRegex.matches(message) ){
            val matchResult = sentMoneyRegex.matchEntire(message)
            var (code, amountSent, paidSent,recipient, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            viewModelScope.launch {
                database.insert(receipt = Receipt(0L,message, code, recipient, null, "sent", convertDateToLong(date+" "+time), time, convertToDouble(balance), convertToDouble(amountSent), null,convertToDouble(transactionCost)) )
            }

//            TODO:1. Insert into the database
            return true
        }else if(receiveMoneyRegex.matches(message)) {
            val matchResult = receiveMoneyRegex.matchEntire(message)
            var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
            time = formatTime(time)
            viewModelScope.launch {
                database.insert(receipt = Receipt(0L, message , code, null, sender, "received", convertDateToLong(date+" "+time), time, convertToDouble(balance), null, convertToDouble(amountReceived), null ) )
            }

            return true
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