package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import android.database.Cursor
import android.provider.Telephony
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.Prefs
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(val application: Application) : ViewModel() {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    private val calendar: Calendar = Calendar.getInstance()
    var receipts: LiveData<List<Receipt>?> = database.getAllReceipts()!!
    var persons: LiveData<List<PersonAndBusiness>> = database.getPeopleAndBusiness()
    var month: String = "Stats for " + months[calendar.get(Calendar.MONTH)]
    var weekExpense: MutableLiveData<Double> = amountTransactedWeek()
    var monthIncome: MutableLiveData<Double> = MutableLiveData(0.0)
    var monthExpenditure: MutableLiveData<Double> = MutableLiveData(0.0)
    var balance: MutableLiveData<Double> = getBalanceM()
    private val prefs = Prefs(application)

    init {

        viewModelScope.launch {
            updateInsertDb()
        }
        calendar.time = Date()
//        processTempReceipts()//For debugging
        amountTransactedMonth()

    }

    private suspend fun updateInsertDb() {
        val lastReceipt = database.getLastReceipt()
        if (lastReceipt != null) {
            val cursor = getCursor()
            cursor?.let {
                val bodyIndex = cursor.getColumnIndexOrThrow("body")
                cursor.moveToFirst()
                val temporaryMessages: MutableList<String> = mutableListOf()
                do {
                    val providerMessage = cursor.getString(bodyIndex)
                    Timber.i("Comparing messages${lastReceipt.message} and $providerMessage")
                    if (lastReceipt.message != providerMessage) {
                        //Put them in a list
                        temporaryMessages.add(0, providerMessage)
                    } else {
                        temporaryMessages.forEach {
                            insertPersonAndReceipt(it, cursor)
                        }
                        break
                    }
    //                        if (cursor.position > 10) break
                } while (cursor.moveToNext());
                cursor.close()
            } ?: Timber.i("Cursor is empty. There are no messages in Mpesa inbox")
        } else {
            //If there is no stored receipt i.e it is the first time or the db has been cleared(reload all messsages
            Timber.i("Inserting all messages")
            val cursor = getCursor()
            cursor?.let {
                val bodyIndex = cursor.getColumnIndexOrThrow("body")
                cursor.moveToFirst()
                do {
                    val providerMessage = cursor.getString(bodyIndex)
                    Timber.i("Message:$providerMessage")
                    insertPersonAndReceipt(providerMessage, cursor)
    //                        if (cursor.position > 10) break
                } while (cursor.moveToNext())
            }
        }
    }

    private fun insertPersonAndReceipt(it: String, cursor: Cursor) {
        val (receipt, person) = buildReceiptFromSms(it)
        viewModelScope.launch {
            if (receipt != null) {
                database.insert(receipt)
                Timber.i("after inserted receipt: Loop" + cursor.position.toString())
            }
            if (person != null) {
                database.insertPerson(person)
                Timber.i("after insert person: Loop" + cursor.position.toString())
            }
        }
    }


    private fun getBalanceM(): MutableLiveData<Double> {
        viewModelScope.launch {
            database.getBalance()?.let {
                balance.value = it
            }
        }
        return MutableLiveData(0.0)
    }

    fun amountTransactedWeek(): MutableLiveData<Double> {
        val todaysDate = getTodaysDate()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val firstDateOfWeek = todaysDate - dayOfWeek
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val lastTimeInADay = "23:59:59"
        val firstTimeInADay = "00:00:00"
        val maximumTime = "$todaysDate/$month/$year $lastTimeInADay"
        val minimumTime = "$firstDateOfWeek/$month/$year $firstTimeInADay"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        var amtTransacted: AmountTransacted? = AmountTransacted(null, null)
        viewModelScope.launch {
            amtTransacted = database.getAmountTransactedList(
                convertDateToLong(minimumTime, dateFormat),
                convertDateToLong(maximumTime, dateFormat)
            )
            weekExpense.value = amtTransacted?.amountSentTotal ?: 2800.0 //elvis expression
//            Timber.i("WEEK expense ${weekExpense.value}")
        }
        return MutableLiveData(0.0)
    }

    private fun amountTransactedMonth() {
        val firstDateMilli = getFirstDayOfMonth(calendar)
        val lastDateMilli = getLastDayOfMonth(calendar)
        Timber.i("FirstDateMilli:" + firstDateMilli)
        Timber.i("LastDateMilli:" + lastDateMilli)

        viewModelScope.launch {
            val amountTransacted = database.getAmountTransactedList(firstDateMilli, lastDateMilli)
            monthIncome.value = amountTransacted?.amountReceivedTotal ?: 0.0
            monthExpenditure.value = amountTransacted?.amountSentTotal ?: 0.0
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


    fun readSMS() {
        val cursor = getCursor()
        cursor?.let {
            val bodyIndex = cursor.getColumnIndexOrThrow("body")
            cursor.moveToFirst()
            do {
                Timber.i("Message:${cursor.getString(bodyIndex)}")
                val (receipt, person) = buildReceiptFromSms(cursor.getString(bodyIndex))
                viewModelScope.launch {
                    if (receipt != null) {
                        database.insert(receipt)
                        Timber.i("after inserted receipt: Loop" + cursor.position.toString())
                    }
                    if (person != null) {
                        database.insertPerson(person)
                        Timber.i("after insert person: Loop" + cursor.position.toString())
                    }
                }
                //TODO:Remove after proof of concept
                if (cursor.position > 100) break
            } while (cursor.moveToNext());
            cursor.close()
        } ?: Timber.i("Cursor is empty. There are no messages in Mpesa inbox")
    }

    private fun getCursor(): Cursor? {
        val uri = Telephony.Sms.Inbox.CONTENT_URI
        val projection = arrayOf("address", "body")
        val selectionClause = "address IN(?,?)"
        val selectionArgs: Array<String> = arrayOf("MPESA")// TODO: Add KCB in the array
        val cursor =
            application.contentResolver.query(uri, projection, selectionClause, selectionArgs, null)
        return cursor
    }

    private fun processTempReceipts() {
        val messages = getTempReceipts()
        for (message in messages) {
            val (receipt, person) = buildReceiptFromSms(message)
            viewModelScope.launch {
                if (receipt != null) {
                    database.insert(receipt)
                }
                if (person != null) {
                    database.insertPerson(person)
                }

            }
        }
    }


}