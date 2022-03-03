package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import timber.log.Timber

class PersonListViewModel(application: Application) : AndroidViewModel(application){
    val database = ReceiptsDatabase.getInstance(application).receiptsDao

    var personAndBusiness: LiveData<List<PersonAndBusiness>> = database.getPeopleAndBusiness()

    fun searchDatabase(searchQuery: String): LiveData<List<PersonAndBusiness>> {
        return database.searchPerson(searchQuery).asLiveData()
    }
    fun onPersonClicked(phoneNo: String) {
        Timber.i("onPersonClickedViewModel"+phoneNo)
    }


}