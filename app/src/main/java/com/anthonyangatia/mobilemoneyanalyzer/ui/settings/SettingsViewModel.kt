package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao

class SettingsViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {

    var person: LiveData<List<Person>> = database.getPeople()

    fun searchDatabase(searchQuery: String): LiveData<List<Person>> {
        return database.searchPerson(searchQuery).asLiveData()
    }

}