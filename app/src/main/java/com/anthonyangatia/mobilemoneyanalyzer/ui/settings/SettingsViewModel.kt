package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import timber.log.Timber

class SettingsViewModel(val database: ReceiptsDao, application: Application): AndroidViewModel(application) {

    private val _navigateToExport = MutableLiveData<Boolean>()
    val navigateToExport: LiveData<Boolean>
        get() = _navigateToExport
    var personAndBusiness: LiveData<List<PersonAndBusiness>> = database.getPeopleAndBusiness()

//    private var _receipts = MutableLiveData<List<Receipt>>()
//    val receipts:LiveData<List<Receipt>?>
//        get() = database.getAllReceipts()
    var receipts: LiveData<List<Receipt>?> = database.getAllReceipts()

    fun searchDatabase(searchQuery: String): LiveData<List<PersonAndBusiness>> {
        return database.searchPerson(searchQuery).asLiveData()
    }

    fun onExportClicked(){
        getReceipts()
        Timber.i("Export receipts clicked")
        _navigateToExport.value = true
    }

    fun onNavigationComplete(){
        _navigateToExport.value = false
    }
    fun onWhiteListClicked(){
        Timber.i("Whitelist clicked")
//        TODO:NOT IMPLEMENTED
    }
    private fun getReceipts(){
        database.getAllReceipts().let {
            it.value?.forEach { receipt ->
                Timber.i(receipt.message)
            }
        }
//        Timber.i("Size: "+_receipts.value?.size)
    }


}