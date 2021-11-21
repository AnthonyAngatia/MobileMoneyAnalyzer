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
//    var amountTransacted: LiveData<AmountTransacted>
    var x = ArrayList<AmountTransacted>()
     var amountTransactedListLiveData = MutableLiveData<List<AmountTransacted>>()

    init{
        var calendar = Calendar.getInstance()
        calendar.time = Date()
        var firstDate = getFirstDayOfMonth(calendar)
        var lastDate = getLastDayOfMonth(calendar)

        receipts = database.getReceiptWhereDate(firstDate, lastDate)!!

        getDate(calendar)

    }

    fun addAmtTransacted(amtTransacted: AmountTransacted){
        x.add(amtTransacted)
        amountTransactedListLiveData.value = x

    }
//    fun amountTransactedPerDay(calendar:Calendar): LiveData<AmountTransacted> {
//        return database.getAmountTransactedList(getDate(calendar)[0], getDate(calendar)[1])!!
//    }
    fun getDate(calendar:Calendar){
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val date = Date()
        println(formatter.format(date))
        val dateRegex = """(\d{1,3})\/(\d{1,3})\/(\d{1,4})\s\d+:\d+:\d+""".toRegex()
        val matchResult = dateRegex.matchEntire(formatter.format(date))
        var (dateR) = matchResult!!.destructured
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


    fun getReceiptsforMonth(){


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
//    val month = calendar.get(Calendar.MONTH) + 1
//        val date  = calendar.get(Calendar.DATE)
//        val year = calendar.get(Calendar.YEAR)
//        val maxTime = "23:59:59"
//        val minTime = "00:00:00"
//
//        viewModelScope.launch {
//            var i = 19
//            for(i in 1..date){

//                var maximumTime = "$i/$month/$year $maxTime"
//                var minimumTime = "$i/$month/$year $minTime"
//                //convert to milli
//                val maxTimeMilli = convertDateToLong(maximumTime)
//                val minTimeMilli = convertDateToLong(minimumTime)
//                amtReceivedPerDayList = mutableListOf()
//                amtSentPerDayList = mutableListOf()


//                dayTransactions = database.getAmountTransactedList(minTimeMilli, maxTimeMilli)!!
//                var amountSent:Double = 0.0
//                var amountReceived:Double = 0.0
//                dayTransactions.forEach {
//                    amountSent = it.amountSent + amountSent
//                    amountReceived = it.amountReceived + amountReceived
//                    amtReceivedPerDayList.add(amountReceived)
//                    amtSentPerDayList.add(amountSent)
//                }
//            }

//        }

}