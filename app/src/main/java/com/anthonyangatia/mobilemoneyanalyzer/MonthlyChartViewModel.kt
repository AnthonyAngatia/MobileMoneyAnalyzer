package com.anthonyangatia.mobilemoneyanalyzer

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import kotlinx.coroutines.launch
import java.util.*

class MonthlyChartViewModel (val database: ReceiptsDao, application: Application): AndroidViewModel(application){
    // TODO: Implement the ViewModel
//    var receipts = database.getReceiptWhereDate(firstDate, lastDate)
    public lateinit var receipts: LiveData<List<Receipt>>

    init{
        var calendar = Calendar.getInstance()
        calendar.time = Date()
        var firstDate = getFirstDayOfMonth(calendar)
        var lastDate = getLastDayOfMonth(calendar)

        viewModelScope.launch {
            receipts = database.getReceiptWhereDate(firstDate, lastDate)!!
        }


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

}