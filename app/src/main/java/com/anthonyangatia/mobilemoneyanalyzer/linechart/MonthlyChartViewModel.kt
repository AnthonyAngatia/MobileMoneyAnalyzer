package com.anthonyangatia.mobilemoneyanalyzer.linechart

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.util.AmountTransacted
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthlyChartViewModel (val database: ReceiptsDao, application: Application): AndroidViewModel(application){
    // TODO: Implement the ViewModel
//    var receipts: LiveData<List<Receipt>>
    var amountTransactedList = ArrayList<AmountTransacted>()
    private var _amountTransactedListLiveData = MutableLiveData<List<AmountTransacted>>()
    val amountTransactedListLiveData:LiveData<List<AmountTransacted>>
        get() = _amountTransactedListLiveData


    init{
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val firstDate = getFirstDayOfMonth(calendar)
        val lastDate = getLastDayOfMonth(calendar)

//        receipts = database.getReceiptWhereDate(firstDate, lastDate)!!

        getAmountTransactedPerDay(calendar)

    }

    fun addAmtTransacted(amtTransacted: AmountTransacted){
        amountTransactedList.add(amtTransacted)
        _amountTransactedListLiveData.value = amountTransactedList //Trigger a change in the live data as we add a new item in the list

    }
    fun getAmountTransactedPerDay(calendar:Calendar){
        val dateI = getTodaysDate()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val lastTimeInADay = "23:59:59"
        val firstTimeInADay = "00:00:00"

     for (i in 1..dateI){
         var maximumTime = "$i/$month/$year $lastTimeInADay"
         var minimumTime = "$i/$month/$year $firstTimeInADay"

         val minTimeMilli = convertDateToLong(minimumTime)
         val maxTimeMilli = convertDateToLong(maximumTime)
//         The transactions are not being processed in a serial order
         viewModelScope.launch {
             var amountTransactedBtwTime = database.getAmountTransactedList(minTimeMilli, maxTimeMilli)
             if (amountTransactedBtwTime != null) {
                 addAmtTransacted(amountTransactedBtwTime)//Add amount transacted to the arraylist
             }else{
                 addAmtTransacted(AmountTransacted(0.0,0.0))
             }
             Timber.i("Day "+ i.toString() +"Amount " + amountTransactedBtwTime.toString())
         }


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

    fun convertDateToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val date = dateFormat.parse(dateString)
        return date.time
    }

    fun getFirstDayOfMonth(calendar: Calendar): Long{
        calendar.set(Calendar.DAY_OF_MONTH,
            calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.timeInMillis

    }
    fun getLastDayOfMonth(calendar: Calendar):Long{
        // set day to maximum
        calendar.set(Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.timeInMillis

    }
}