package com.anthonyangatia.mobilemoneyanalyzer.linechart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao

class BarChartViewModelFactory(
    private val dataSource: ReceiptsDao,
    private val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarChartViewModel::class.java)) {
            return BarChartViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
