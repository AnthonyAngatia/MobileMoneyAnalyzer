package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.ReceiptsAdapter
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = HomeViewModelFactory(application)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        homeViewModel.weekExpense.observe(viewLifecycleOwner, {
            it?.let{
                binding.amtSpentTextView.text = it.toString()
            }
        })


        val adapter = ReceiptsAdapter()

        observeReceipts(adapter)
//        observePeople()





        return this.binding.root
    }

    private fun observeReceipts(adapter: ReceiptsAdapter) {
        homeViewModel.receipts?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.receiptList = it
            }
        })
    }

    private fun observePeople() {
//        homeViewModel.persons.observe(viewLifecycleOwner, {
//            for (person in it) {
//                this.binding.testTextView.text = person.toString()
//                Timber.i(person.toString())
//            }
//        })
    }
}