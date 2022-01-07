package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.compare

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.util.AmountTransacted
import kotlinx.coroutines.launch
import timber.log.Timber

class CompareViewModel( application: Application) : AndroidViewModel(application) {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var personA: MutableLiveData<PersonAndBusiness> = MutableLiveData()
    var personB:MutableLiveData<PersonAndBusiness> = MutableLiveData()
    var amtTransA:MutableLiveData<AmountTransacted> = MutableLiveData()
    var amtTransB:MutableLiveData<AmountTransacted> = MutableLiveData()

    var isPersonASet = false
    var isPersonBSet = false

    var personAndBusiness: LiveData<List<PersonAndBusiness>> = database.getPeopleAndBusiness()

    fun searchDatabase(searchQuery: String): LiveData<List<PersonAndBusiness>> {
        return database.searchPerson(searchQuery).asLiveData()
    }
    fun onPersonClicked(id: String) {
        Timber.i("onPersonClicked"+id)
    }
    fun getPersonA(name:String) {
        if(!isPersonASet){
            viewModelScope.launch {
                Timber.i("Get personA viewmodel scope")
                personA.value =  database.getPerson(name)
            }
        }
        isPersonASet = true
    }

    fun getPersonB(name:String) {
        viewModelScope.launch {
            Timber.i("Get personB viewmodel scope")

            personB.value =  database.getPerson(name)
        }
        isPersonBSet = true
    }

     fun getAmountTransactedA(name: String){
         viewModelScope.launch {
//             Timber.i("Get amtTransa viewmodel scope")
             amtTransA.value = database.getSumOfTransactionOfPerson(name)
        }
    }
    fun getAmountTransactedB(name: String){
        viewModelScope.launch {
//            Timber.i("Get amtTransB viewmodel scope")
            amtTransB.value = database.getSumOfTransactionOfPerson(name)
        }
    }
}