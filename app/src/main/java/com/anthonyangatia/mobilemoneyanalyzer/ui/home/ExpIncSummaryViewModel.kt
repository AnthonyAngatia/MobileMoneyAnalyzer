package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ExpIncSummaryViewModel(application: Application) : AndroidViewModel(application) {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    private val calendar: Calendar = Calendar.getInstance()
    val expenseListLiveData: MutableLiveData<List<PersonAmountTransacted>> = MutableLiveData() //Get Person with highest expenses
    val incomeListLiveData: MutableLiveData<List<PersonAmountTransacted>> = MutableLiveData()
    val weekReceipts:MutableLiveData<List<Receipt>> = MutableLiveData()
    val personAmountTransactedList:MutableList<PersonAmountTransacted> = mutableListOf()


    init{
        //Pass receipt to get income and expense list
        getReceiptsWeek()
//        if (weekReceipts != null) {
//            weekReceipts.value?.let { expenseIncomeList(it) }
//        }

    }

    fun getReceiptsWeek() {
        val todaysDate = getTodaysDate()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1
        val firstDateOfWeek = todaysDate-dayOfWeek
//        val (minTimeMilli, maxTimeMilli) = getMinMaxTimeWeek( todaysDate, firstDateOfWeek)
        val minTimeMilli = getFirstDayOfMonth(calendar)
        val maxTimeMilli = getLastDayOfMonth(calendar)
        Timber.i("minTime:"+ minTimeMilli.toString())//1641502800000
        Timber.i("maxTime:"+ maxTimeMilli.toString())//1641157199000

        viewModelScope.launch {
            database.getReceiptWhereDate(minTimeMilli, maxTimeMilli)?.let {
                weekReceipts.value = it
                Timber.i("Size of it"+it.size.toString())
                expenseIncomeList(it)
            }
        }
    }
    fun getReceiptsMonth(){
        val minTimeMilli = getFirstDayOfMonth(calendar)
        val maxTimeMilli = getLastDayOfMonth(calendar)
        viewModelScope.launch {
            Timber.i("Inviewmodelscope")
            database.getReceiptWhereDate(minTimeMilli, maxTimeMilli)?.let {
                Timber.i("Some receipts are in")
                weekReceipts.value = it
                expenseIncomeList(it)
            }
        }
    }

    fun expenseIncomeList(receipts:List<Receipt>){
        //Get all receitps
        //Push receipts to PersonAndAmountTransacted, where phoneNumber is the same, sum the amount sent or amount received
        //Set personAmountTransacted as the livedata
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
           receipt.name?.let {
               viewModelScope.launch {
                   Timber.i("Getting person viewmodelscopelaunch:"+it)
                   val person = database.getPerson(it)
                   addToPATList(person, amountTransacted)
               }
           }
       }
    }

    private fun addToPATList(personAndBusiness: PersonAndBusiness, amountTransacted: AmountTransacted) {
        val personAmountTransacted = PersonAmountTransacted(personAndBusiness, amountTransacted)
        var personFound = false
        Timber.i("Approaching for loop empty then wont work")
        for (pAT in personAmountTransactedList) {
            Timber.i("Checking if person is in the list")
            if (pAT.personAndBusiness.name == personAmountTransacted.personAndBusiness.name) {//In the real world we can have people with similar names
                pAT.amountTransacted.apply {
                    amountSentTotal = 100.0
                    amountReceivedTotal = 20.0
                }
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
        if (!personFound){
            Timber.i("Person Added to list")
            personAmountTransactedList.add(personAmountTransacted)
        }
        //TODO: The idea of sorting is very buggy and should be relooked in the feature
        personAmountTransactedList.sortByDescending {
            it.amountTransacted.amountReceivedTotal
        }
        val incomeList = personAmountTransactedList
        Timber.i("Size of income list:"+incomeList.size.toString())//
        incomeListLiveData.value = incomeList

        personAmountTransactedList.sortByDescending {
            it.amountTransacted.amountSentTotal
        }
        val expenseList = personAmountTransactedList
        Timber.i("Size of expense list:"+expenseList.size.toString())
        expenseListLiveData.value = expenseList

    }





}