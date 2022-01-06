package com.anthonyangatia.mobilemoneyanalyzer.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.SearchFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.util.onQueryTextChanged


class SearchFragment : Fragment(){

    private lateinit var binding:SearchFragmentBinding
    private lateinit var searchViewModel:SearchViewModel

    private val searchAdapter = SearchAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = SearchViewModelFactory(dataSource, application)
        searchViewModel =ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

//        val adapter = searchAdapter
        binding.personSearchListSearch.adapter = searchAdapter



        searchViewModel.receipts?.observe(viewLifecycleOwner, Observer{
            it?.let{
                searchAdapter.receiptList = it
//                Timber.i(it.toString())
            }
        })
        val searchView = binding.searchView
        searchView.isSubmitButtonEnabled = true
        searchView.onQueryTextChanged{
            searchDatabase(it)
        }

        return binding.root
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        searchViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, {
            it?.let {
                searchAdapter.receiptList = it
            }
        })
    }
}

