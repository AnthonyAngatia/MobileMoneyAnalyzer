package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.ReceiptsAdapter
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = HomeViewModelFactory(application)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        return this.binding.root
    }

//    private fun observeReceipts(adapter: ReceiptsAdapter) {
//        homeViewModel.receipts.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.receiptList = it
//            }
//        })
//    }

    private fun observePeople() {
//        homeViewModel.persons.observe(viewLifecycleOwner, {
//            for (person in it) {
//                this.binding.testTextView.text = person.toString()
//                Timber.i(person.toString())
//            }
//        })
    }
}
class HomeViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}