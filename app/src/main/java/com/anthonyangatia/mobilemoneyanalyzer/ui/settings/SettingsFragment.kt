package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = SettingsViewModelFactory(dataSource,application)
        settingsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.compareCardView.setOnClickListener{ view->
            view.findNavController().navigate(R.id.action_navigation_settings_to_compareFragment)

        }
        binding.setTargetCardView.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_settings_to_personListFragment)
        }

//        binding.settingsList.adapter = personsAdapter
//
//        settingsViewModel.person.observe(viewLifecycleOwner, {
//            it?.let{
//                personsAdapter.personList = it
////                Timber.i(it.toString())
//            }
//        })
//
//        val searchView = binding.settingsSearchView
//        searchView.isSubmitButtonEnabled = true
//        searchView.onQueryTextChanged{
//            searchDatabase(it)
//        }


        return root
    }

}

class SettingsViewModelFactory(val dataSource: ReceiptsDao, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}