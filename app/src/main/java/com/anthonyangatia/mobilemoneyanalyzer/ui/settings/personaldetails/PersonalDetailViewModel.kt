package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.personaldetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.Target
import kotlinx.coroutines.launch

class PersonalDetailViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application)  {
    var receipts: LiveData<List<Receipt>>
    var moneySent: MutableLiveData<Double> = MutableLiveData(0.0)
    var moneyReceived:MutableLiveData<Double> = MutableLiveData(0.0)
    var targets: LiveData<List<Target>> = database.getTargetOfPerson("0721115067")//TODO:  Create a vieww for this



    init {
        receipts = database.getReceiptsWherePerson("0721115067")
        viewModelScope.launch {
            val amountTransacted = database.getSumOfTransactionOfPerson("0721115067")
            moneySent.value = amountTransacted?.amountSentTotal!!
            moneyReceived.value = amountTransacted.amountReceivedTotal!!
        }
    }

    fun updateExpense(target:Double){
        viewModelScope.launch {
            database.updateExpense("0721115067", target)
        }
    }


}