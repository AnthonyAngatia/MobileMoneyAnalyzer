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
import com.anthonyangatia.mobilemoneyanalyzer.util.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class HomeViewModel(val application: Application) : ViewModel() {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    private val calendar: Calendar = Calendar.getInstance()
    var business: LiveData<List<Business>> = database.getBusiness()
    var receipts:LiveData<List<Receipt>> = database.getAllReceipts()!!
    var persons:LiveData<List<Person>> = database.getPeople()
    var month: String
    var weekExpense:MutableLiveData<Double> = amountTransactedWeek()
    var monthIncome:String = "12309"
     var monthExpenditure:String = "120"
     var balance:Int = getBalanceM()
    private val prefs = Prefs(application)

    init {

        prefs.newPhone = true //For debugging purpose

        calendar.time = Date()
        month = "Stats for "+months[calendar.get(Calendar.MONTH)]
        if(prefs.newPhone){
            viewModelScope.launch {
                database.clear()
                database.clearPerson()
                database.clearBusiness()
//                readSMS()
                prefs.newPhone = false
            }
        }else{
//            TODO: Check whether the last receipt in content provider is the same as the one in my database
//            If not, write a recursive algorithm that tries to establish the last message
        }
        processTempReceipts()//For debugging
        amountTransactedMonth()

    }

    private fun getBalanceM(): Int {
        var bal:Double? = null
        viewModelScope.launch {
            bal = database.getBalance()
            if (bal == null) bal = 200.0
        }
        return bal?.toInt() ?: 190
    }

    fun amountTransactedWeek(): MutableLiveData<Double> {
        val todaysDate = getTodaysDate()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1
        val firstDateOfWeek = todaysDate-dayOfWeek
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val lastTimeInADay = "23:59:59"
        val firstTimeInADay = "00:00:00"
        val maximumTime = "$todaysDate/$month/$year $lastTimeInADay"
        val minimumTime = "$firstDateOfWeek/$month/$year $firstTimeInADay"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        var amtTransacted:AmountTransacted? = AmountTransacted(null,null)
        viewModelScope.launch {
            amtTransacted = database.getAmountTransactedList(convertDateToLong(minimumTime,dateFormat), convertDateToLong(maximumTime,dateFormat))
//            weekExpense.value = amtTransacted?.amountSentTotal ?: 2800.0 //elvis expression
//            Timber.i("WEEK expense ${weekExpense.value}")
        }
        return MutableLiveData(amtTransacted?.amountSentTotal ?: 190.0)
    }

    private fun amountTransactedMonth(){
        val firstDateMilli = getFirstDayOfMonth(calendar)
        val lastDateMilli = getLastDayOfMonth(calendar)
        viewModelScope.launch {
            val amountTransacted = database.getAmountTransactedList(firstDateMilli,lastDateMilli)
            monthIncome = amountTransacted?.amountReceivedTotal?.toString() ?: "0.0"
            monthExpenditure = amountTransacted?.amountSentTotal?.toString() ?: "0.0"
        }

    }


    private fun getTodaysDate(): Int {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val date = Date()
        println(formatter.format(date))
        val dateRegex = """(\d{1,3})\/(\d{1,3})\/(\d{1,4})\s\d+:\d+:\d+""".toRegex()
        val matchResult = dateRegex.matchEntire(formatter.format(date))
        val (dateR) = matchResult!!.destructured
        return dateR.toInt()
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