package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao

class SettingsViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {

    private val _navigateToExport = MutableLiveData<Boolean>()
    val navigateToExport: LiveData<Boolean>
    get() = _navigateToExport
    var personAndBusiness: LiveData<List<PersonAndBusiness>> = database.getPeopleAndBusiness()

    fun searchDatabase(searchQuery: String): LiveData<List<PersonAndBusiness>> {
        return database.searchPerson(searchQuery).asLiveData()
    }

    fun onExportClicked(){
        _navigateToExport.value = true
    }

    fun onNavigationComplete(){
        _navigateToExport.value = false
    }
    fun onWhiteListClicked(){
//        TODO:NOT IMPLEMENTED
    }


}