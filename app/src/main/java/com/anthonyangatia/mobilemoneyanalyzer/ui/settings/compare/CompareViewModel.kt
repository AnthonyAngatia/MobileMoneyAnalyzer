package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.compare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import timber.log.Timber

class CompareViewModel( application: Application) : AndroidViewModel(application) {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao

    var person: LiveData<List<Person>> = database.getPeople()

    fun searchDatabase(searchQuery: String): LiveData<List<Person>> {
        return database.searchPerson(searchQuery).asLiveData()
    }
    fun onPersonClicked(id: String) {
        Timber.i("onPersonClicked"+id)
    }
}