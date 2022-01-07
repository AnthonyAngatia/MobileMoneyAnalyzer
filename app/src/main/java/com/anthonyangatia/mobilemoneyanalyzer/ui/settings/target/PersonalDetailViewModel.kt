package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.Target
import kotlinx.coroutines.launch

class PersonalDetailViewModel(val database: ReceiptsDao, application: Application, val name: String): AndroidViewModel(application)  {
    var receipts: LiveData<List<Receipt>> = database.getReceiptsWherePerson(name)
    var moneySent: MutableLiveData<Double> = MutableLiveData(0.0)
    var moneyReceived:MutableLiveData<Double> = MutableLiveData(0.0)
    var targets: LiveData<List<Target>> = database.getTargetOfPerson(name)//TODO:  Create a vieww for this



    init {
        viewModelScope.launch {
            val amountTransacted = database.getSumOfTransactionOfPerson(name)
            moneySent.value = amountTransacted?.amountSentTotal ?: 199.0
            moneyReceived.value = amountTransacted?.amountReceivedTotal ?:199.2
        }
    }

    fun updateExpense(target:Double){
        viewModelScope.launch {
            database.updateExpense(name, target)
        }
    }


}