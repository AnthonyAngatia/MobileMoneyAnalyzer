package com.anthonyangatia.mobilemoneyanalyzer

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthlyChartViewModel (val database: ReceiptsDao, application: Application): AndroidViewModel(application){
    // TODO: Implement the ViewModel
    var receipts: LiveData<List<Receipt>>
    var x = ArrayList<AmountTransacted>()
     var amountTransactedListLiveData = MutableLiveData<List<AmountTransacted>>()

    init{
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val firstDate = getFirstDayOfMonth(calendar)
        val lastDate = getLastDayOfMonth(calendar)

        receipts = database.getReceiptWhereDate(firstDate, lastDate)!!

        getDate(calendar)

    }

    fun addAmtTransacted(amtTransacted: AmountTransacted){
        x.add(amtTransacted)
        amountTransactedListLiveData.value = x

    }
    fun getDate(calendar:Calendar){
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val date = Date()
        println(formatter.format(date))
        val dateRegex = """(\d{1,3})\/(\d{1,3})\/(\d{1,4})\s\d+:\d+:\d+""".toRegex()
        val matchResult = dateRegex.matchEntire(formatter.format(date))
        val (dateR) = matchResult!!.destructured
        val month = calendar.get(Calendar.MONTH) + 1
        val dateI  = dateR.toInt()
        val year = calendar.get(Calendar.YEAR)
        val maxTime = "23:59:59"
        val minTime = "00:00:00"

     for (i in 1..dateI){
         var maximumTime = "$i/$month/$year $maxTime"
         var minimumTime = "$i/$month/$year $minTime"

         val minTimeMilli = convertDateToLong(minimumTime)
         val maxTimeMilli = convertDateToLong(maximumTime)
         viewModelScope.launch {
             var a = database.getAmountTransactedList(minTimeMilli, maxTimeMilli)
             if (a != null) {
                 addAmtTransacted(a)
             }else{
                 addAmtTransacted(AmountTransacted(0.0,0.0))
             }
         }


     }
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