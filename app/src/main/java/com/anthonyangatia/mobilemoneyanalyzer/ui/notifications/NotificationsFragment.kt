package com.anthonyangatia.mobilemoneyanalyzer.ui.notifications

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var binding: FragmentNotificationsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = NotificationsViewModelFactory(dataSource, application)
        val notificationsViewModel = ViewModelProvider(this, viewModelFactory).get(NotificationsViewModel::class.java)

//
        return binding.root
    }


}

class NotificationsViewModelFactory(val dataSource: ReceiptsDao, val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}
