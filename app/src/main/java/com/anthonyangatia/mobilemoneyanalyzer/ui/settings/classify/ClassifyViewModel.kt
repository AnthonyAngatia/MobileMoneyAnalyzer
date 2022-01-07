package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.classify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import kotlinx.coroutines.launch
import timber.log.Timber

class ClassifyViewModel(application: Application, val receiptId: Long): AndroidViewModel(application)  {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var receipt = database.getReceipt(receiptId)
    val message = receipt.value?.message ?: "Message default"

    fun classifyReceipt(category:String){
        viewModelScope.launch {
            database.classify(category, receiptId)
        }
    }




    // TODO: Implement the ViewModel
}