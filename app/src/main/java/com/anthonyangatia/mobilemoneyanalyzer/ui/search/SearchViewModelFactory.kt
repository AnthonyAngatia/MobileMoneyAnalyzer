package com.anthonyangatia.mobilemoneyanalyzer.ui.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.ReceiptViewModel
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao

class SearchViewModelFactory(val dataSource: ReceiptsDao, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
