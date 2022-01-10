package com.anthonyangatia.mobilemoneyanalyzer.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.database.Target

class NotificationsViewModel (application: Application): AndroidViewModel(application) {
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var targets: LiveData<List<Target>> = database.getAllTargets()



}