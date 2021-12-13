package com.anthonyangatia.mobilemoneyanalyzer.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.ReceiptsAdapter
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentSmsReceiptsBinding
import com.anthonyangatia.mobilemoneyanalyzer.databinding.SearchFragmentBinding
import timber.log.Timber

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = SearchViewModelFactory(dataSource, application)
        val searchViewModel =ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        val adapter = SearchAdapter()
        binding.searchList.adapter = adapter

        searchViewModel.receipts?.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.receiptList = it
//                Timber.i(it.toString())
            }
        })
        return binding.root
    }

}