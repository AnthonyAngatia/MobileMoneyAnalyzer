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

    private val notificationsViewModel: NotificationsViewModel by lazy { ViewModelProvider(this).get(NotificationsViewModel::class.java)  }
    private lateinit var binding: FragmentNotificationsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)


//
        return binding.root
    }


}