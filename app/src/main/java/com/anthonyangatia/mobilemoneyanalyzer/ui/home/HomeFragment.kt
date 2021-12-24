package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.ReceiptsAdapter
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.testTextView

        val application = requireNotNull(this.activity).application
        val viewModelFactory = HomeViewModelFactory(application)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val adapter = ReceiptsAdapter()

        observeReceipts(adapter)
//        observePeople()
        homeViewModel.business.observe(viewLifecycleOwner, {
            for (business in it) {
                binding.testTextView.text = business.toString()
                Timber.i(business.toString())
            }
        })



        return binding.root
    }

    private fun observeReceipts(adapter: ReceiptsAdapter) {
        homeViewModel.receipts?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.receiptList = it
            }
        })
    }

    private fun observePeople() {
        homeViewModel.persons.observe(viewLifecycleOwner, {
            for (person in it) {
                binding.testTextView.text = person.toString()
                Timber.i(person.toString())
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}