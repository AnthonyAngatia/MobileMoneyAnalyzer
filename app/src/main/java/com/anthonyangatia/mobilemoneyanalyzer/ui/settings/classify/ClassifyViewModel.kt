package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.classify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import timber.log.Timber

class ClassifyViewModel(application: Application, val receiptId: Long): AndroidViewModel(application)  {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var receipt = database.getReceipt(receiptId)
    val message = receipt.value?.message ?: "Message default"

    fun classifyReceipt(text:String?){
        Timber.i("Class func called")
    }




    // TODO: Implement the ViewModel
}