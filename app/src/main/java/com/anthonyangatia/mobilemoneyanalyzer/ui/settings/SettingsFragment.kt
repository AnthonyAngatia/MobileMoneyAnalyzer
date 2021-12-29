package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

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
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentSettingsBinding
import com.anthonyangatia.mobilemoneyanalyzer.ui.search.onQueryTextChanged

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding

    val settingsAdapter = SettingsAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = SettingsViewModelFactory(dataSource,application)
        settingsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.settingsList.adapter = settingsAdapter

        settingsViewModel.person.observe(viewLifecycleOwner, {
            it?.let{
                settingsAdapter.personList = it
//                Timber.i(it.toString())
            }
        })

        val searchView = binding.settingsSearchView
        searchView.isSubmitButtonEnabled = true
        searchView.onQueryTextChanged{
            searchDatabase(it)
        }


        return root
    }
    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        settingsViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, {
            it?.let {
                settingsAdapter.personList = it
            }
        })
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