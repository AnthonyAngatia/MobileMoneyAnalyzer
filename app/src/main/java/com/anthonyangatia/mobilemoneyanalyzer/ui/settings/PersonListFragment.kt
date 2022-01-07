package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.databinding.PersonListFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.util.onQueryTextChanged
import timber.log.Timber

class PersonListFragment : Fragment() {



    private val viewModel by lazy {  ViewModelProvider(this).get(PersonListViewModel::class.java) }
    private lateinit var binding: PersonListFragmentBinding
    private lateinit var personsAdapter:PersonsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PersonListFragmentBinding.inflate(inflater, container, false)

        personsAdapter = PersonsAdapter(PersonListener { person ->
            Timber.i("At adapter"+person.toString())
//            Toast.makeText(context, person.toString(), Toast.LENGTH_SHORT).show()


            viewModel.onPersonClicked(person.name)

        })
        binding.personSearchListCompare.adapter = personsAdapter
        viewModel.personAndBusiness.observe(viewLifecycleOwner, {
            it?.let{
                personsAdapter.submitList(it)
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
                personsAdapter.submitList(it)
            }
        })
    }


}