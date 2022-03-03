package com.anthonyangatia.mobilemoneyanalyzer.piechart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.util.AmountTransacted
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class PieViewModel(val database: ReceiptsDao, application: Application) : AndroidViewModel(
    application
) {
    var totalAmtTypeSent  = MutableLiveData<AmountTransacted>()

    //    var totalAmtReceived = ArrayList<AmountTransacted>()
    var totalAmtTypeReceived = MutableLiveData<AmountTransacted>()
    init {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val firstDate = getFirstDayOfMonth(calendar)
        val lastDate = getLastDayOfMonth(calendar)
        viewModelScope.launch {
            database.getSumOfTransactionType(firstDate,lastDate, "received")?.let {
                totalAmtTypeReceived.value = it
            }
            database.getSumOfTransactionType(firstDate,lastDate, "sent")?.let {
                totalAmtTypeSent.value = it
            }
        }
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