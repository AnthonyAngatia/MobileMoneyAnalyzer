package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao

class SettingsViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {

    var personAndBusiness: LiveData<List<PersonAndBusiness>> = database.getPeopleAndBusiness()

    fun searchDatabase(searchQuery: String): LiveData<List<PersonAndBusiness>> {
        return database.searchPerson(searchQuery).asLiveData()
    }

}