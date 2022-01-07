package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpIncSummaryViewModel(application: Application) : AndroidViewModel(application) {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    private val calendar: Calendar = Calendar.getInstance()
    val expenseListLiveData: MutableLiveData<List<PersonAmountTransacted>> = MutableLiveData() //Get Person with highest expenses
    val incomeListLiveData: MutableLiveData<List<PersonAmountTransacted>> = MutableLiveData()
    val weekReceipts:LiveData<List<Receipt>>? = getReceiptsWeek()

    init{
        //Pass receipt to get income and expense list
        if (weekReceipts != null) {
            weekReceipts.value?.let { expenseIncomeList(it) }
        }

    }



    fun getReceiptsWeek(): LiveData<List<Receipt>>? {
        val todaysDate = getTodaysDate()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1
        val firstDateOfWeek = todaysDate-dayOfWeek
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val lastTimeInADay = "23:59:59"
        val firstTimeInADay = "00:00:00"
        var maximumTime = "$todaysDate/$month/$year $lastTimeInADay"
        var minimumTime = "$firstDateOfWeek/$month/$year $firstTimeInADay"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return database.getReceiptWhereDate(convertDateToLong(minimumTime, dateFormat), convertDateToLong(maximumTime, dateFormat))
    }
    fun getReceiptsMonth(): LiveData<List<Receipt>>? {
        val firstDateMilli = getFirstDayOfMonth(calendar)
        val lastDateMilli = getLastDayOfMonth(calendar)
        return database.getReceiptWhereDate(firstDateMilli, lastDateMilli)
    }

    fun expenseIncomeList(receipts:List<Receipt>){
        //Get all receitps
        //Push receipts to PersonAndAmountTransacted, where phoneNumber is the same, sum the amount sent or amount received
        //Set personAmountTransacted as the livedata
        val personAmountTransactedList:MutableList<PersonAmountTransacted> = mutableListOf()
       for(receipt in receipts){
           var amountSent = 0.0
           var amountReceived = 0.0
           receipt.amountSent?.let {
               amountSent = it
           }
           receipt.amountReceived?.let {
               amountReceived = it
           }
           val amountTransacted = AmountTransacted(amountSent, amountReceived)
           receipt.phoneNumber?.let {
               viewModelScope.launch {
                   val person = database.getPerson(it)
                   addToPATList(person, amountTransacted, personAmountTransactedList)
               }
           }
       }
        personAmountTransactedList.sortByDescending {
            it.amountTransacted.amountReceivedTotal
        }
        val incomeList = personAmountTransactedList
        incomeListLiveData.value = incomeList

        personAmountTransactedList.sortByDescending {
            it.amountTransacted.amountSentTotal
        }
        val expenseList = personAmountTransactedList
        expenseListLiveData.value = expenseList


    }

    private fun addToPATList(
        person: Person,
        amountTransacted: AmountTransacted,
        personAmountTransactedList: MutableList<PersonAmountTransacted>
    ) {
        var personAmountTransacted = PersonAmountTransacted(person, amountTransacted)
        var personFound = false
        for (pAT in personAmountTransactedList) {
            if (pAT.person == personAmountTransacted.person) {
                pAT.amountTransacted.amountSentTotal.let {
                    it!! + personAmountTransacted.amountTransacted.amountSentTotal!!
                }
                pAT.amountTransacted.amountReceivedTotal.let {
                    it!! + personAmountTransacted.amountTransacted.amountReceivedTotal!!
                }
                personFound = true
                break
            } else {

            }
        }
        if (!personFound) personAmountTransactedList.add(personAmountTransacted)
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





}