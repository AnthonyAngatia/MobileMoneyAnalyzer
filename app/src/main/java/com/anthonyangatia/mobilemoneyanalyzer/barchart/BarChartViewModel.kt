package com.anthonyangatia.mobilemoneyanalyzer.linechart

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.util.AmountTransacted
import com.anthonyangatia.mobilemoneyanalyzer.util.daysOfWeek
import com.anthonyangatia.mobilemoneyanalyzer.util.getMinMaxTimeDay
import com.anthonyangatia.mobilemoneyanalyzer.util.getTodaysDate
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class BarChartViewModel (val database: ReceiptsDao, application: Application): AndroidViewModel(application){
//    var amountTransactedList = ArrayList<AmountTransacted>()
//    private var _amountTransactedListLiveData = MutableLiveData<List<AmountTransacted>>()
//    val amountTransactedListLiveData:LiveData<List<AmountTransacted>>
//        get() = _amountTransactedListLiveData
    var amtTransactedWeekLive: MutableLiveData<HashMap<String, AmountTransacted>> = MutableLiveData()
    var amtTransactedWeek: HashMap<String, AmountTransacted> = HashMap()
    val calendar = Calendar.getInstance()



    init{
//        receipts = database.getReceiptWhereDate(firstDate, lastDate)!!
//        getAmountTransactedPerDay(calendar)
        getAmountTransactedWeek()
        amtTransactedWeekLive.value = amtTransactedWeek

    }
    fun insertHashMap(dayOfWeek:String, amtTransacted: AmountTransacted){
        amtTransactedWeek.size
        amtTransactedWeek.put(dayOfWeek, amtTransacted)
        amtTransactedWeekLive.value = amtTransactedWeek
    }



    fun getAmountTransactedWeek(){
        val dateI = getTodaysDate()
        viewModelScope.launch {
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) -1
        for (day in dateI downTo (dateI-6)){
            if(dayOfWeek < 0) dayOfWeek = 6
            if (day > 0){
                val (minTimeMilli, maxTimeMilli) = getMinMaxTimeDay(day)
                //         The transactions are not being processed in a serial order
                    var amountTransactedBtwTime = database.getAmountTransactedList(minTimeMilli, maxTimeMilli)
                    if (amountTransactedBtwTime != null) {
                        insertHashMap(dayOfWeekStr(dayOfWeek), amountTransactedBtwTime)
                    }else{
                        insertHashMap(dayOfWeekStr(dayOfWeek), AmountTransacted(0.0,0.0) )
                    }
//                    Timber.i("Day "+ day.toString() +"Amount " + amountTransactedBtwTime.toString())

            }else{
//                TODO:Handle this exception later
//                Example if date is 3, the loop goes to date 0 which does not exist
            }
            dayOfWeek--
        }
        }



    }

     fun dayOfWeekStr(index:Int):String{
         return daysOfWeek[index]
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