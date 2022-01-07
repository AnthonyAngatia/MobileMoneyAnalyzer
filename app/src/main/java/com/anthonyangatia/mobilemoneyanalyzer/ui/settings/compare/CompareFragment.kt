package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.compare

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.databinding.CompareFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.ui.settings.PersonsAdapter

class CompareFragment : Fragment() {

    private lateinit var viewModel: CompareViewModel
    private lateinit var binding:CompareFragmentBinding

    lateinit var personsAdapter:PersonsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CompareFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CompareViewModel::class.java)



//        binding.fragmentPersonList.personSearchListCompare.adapter = personsAdapter


        viewModel.personAndBusiness.observe(viewLifecycleOwner, {
            it?.let{
                personsAdapter.submitList(it)
            }
        })

//        val searchView = binding.searchViewComparison
//        searchView.isSubmitButtonEnabled = true
//        searchView.onQueryTextChanged{
//            searchDatabase(it)
//        }

        return binding.root
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, {
            it?.let {
                personsAdapter.submitList(it)
            }
        })
    }


}