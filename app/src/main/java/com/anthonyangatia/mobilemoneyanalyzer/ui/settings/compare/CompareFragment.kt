package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.compare

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.databinding.CompareFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.ui.settings.PersonListener
import com.anthonyangatia.mobilemoneyanalyzer.ui.settings.PersonsAdapter
import com.anthonyangatia.mobilemoneyanalyzer.util.onQueryTextChanged
import timber.log.Timber

class CompareFragment : Fragment() {

    private lateinit var viewModel: CompareViewModel
    private lateinit var binding:CompareFragmentBinding

    lateinit var adapter:PersonsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CompareFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CompareViewModel::class.java)
        binding.compareViewModel = viewModel
        adapter = PersonsAdapter(PersonListener { person ->
            Timber.i("At adapter"+person.toString())
//            Toast.makeText(context, person.toString(), Toast.LENGTH_SHORT).show()
            viewModel.onPersonClicked(person.name)

        },"CompareFragment", viewModel)
        binding.personSearchListCompare.adapter = adapter

        viewModel.personAndBusiness.observe(viewLifecycleOwner, {
            it?.let{
                adapter.submitList(it)
            }
        })
        viewModel.personA.observe(viewLifecycleOwner, {
            it?.let {
                val name = it.name
                binding.nameLeft.text = name
                viewModel.getAmountTransactedA(name)
            }
        })
        viewModel.personB.observe(viewLifecycleOwner, {
            it?.let {
                val name = it.name
                binding.nameRight.text = name
                viewModel.getAmountTransactedB(name)
            }
        })
        viewModel.amtTransA.observe(viewLifecycleOwner, {
            it?.let {
              binding.moneySentLeft.text = it.amountSentTotal.toString()
                binding.moneyReceivedLeft.text = it.amountReceivedTotal.toString()
            }
        })
        viewModel.amtTransB.observe(viewLifecycleOwner, {
            it?.let {
                binding.moneySentRight.text = it.amountSentTotal.toString()
                binding.moneyReceivedRight.text = it.amountReceivedTotal.toString()
            }
        })
        val searchView = binding.searchViewComparison
        searchView.isSubmitButtonEnabled = true
        searchView.
        onQueryTextChanged{
            searchDatabase(it)
        }
        return binding.root
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, {
            it?.let {
                Timber.i(query)
                adapter.submitList(it)
            }
        })
    }


}