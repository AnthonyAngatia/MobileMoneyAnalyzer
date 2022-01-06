package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import timber.log.Timber

class PersonListViewModel(application: Application) : AndroidViewModel(application){
    val database = ReceiptsDatabase.getInstance(application).receiptsDao

    var person: LiveData<List<Person>> = database.getPeople()

    fun searchDatabase(searchQuery: String): LiveData<List<Person>> {
        return database.searchPerson(searchQuery).asLiveData()
    }
    fun onPersonClicked(phoneNo: String) {
        Timber.i("onPersonClickedViewModel"+phoneNo)
    }


}