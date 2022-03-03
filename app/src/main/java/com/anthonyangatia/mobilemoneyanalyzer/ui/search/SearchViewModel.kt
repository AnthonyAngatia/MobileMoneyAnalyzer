package com.anthonyangatia.mobilemoneyanalyzer.ui.search

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.util.buildReceiptFromSms
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList

class SearchViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {
    var receipts: LiveData<List<Receipt>?>

    init {
        receipts = database.getAllReceipts()!!
    }


    fun searchDatabase(searchQuery: String): LiveData<List<Receipt>> {
        return database.searchReceipt(searchQuery).asLiveData()
    }

}