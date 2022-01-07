package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.target

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.ReceiptsAdapter
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentPersonDetailBinding

class PersonDetailFragment : Fragment() {
    
    lateinit var binding:FragmentPersonDetailBinding
    lateinit var personalDetailViewModel:PersonalDetailViewModel
    val adapter:ReceiptsAdapter = ReceiptsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPersonDetailBinding.inflate(inflater, container, false)

        binding.personTransactionList.adapter = adapter
        val application = requireNotNull(this.activity).application
        val database = ReceiptsDatabase.getInstance(application).receiptsDao
        val args = PersonDetailFragmentArgs.fromBundle(requireArguments())
        val name = args.name
        val viewModelFactory = PersonalDetailViewModelFactory(database, application, name)
        personalDetailViewModel = ViewModelProvider(this, viewModelFactory).get(PersonalDetailViewModel::class.java)





        binding.personNameTv.text = "Set Name From Args"
        binding.button.setOnClickListener {
            if(binding.targetAmt.text != null){
                personalDetailViewModel.updateExpense(binding.targetAmt.text.toString().toDouble())
            }
        }

        ///Work on Spinner item
//        binding.targetSpinner.setOnItemClickListener()
        observeMoneySentAndReceived()

        observeReceiptsOfPerson()

        return binding.root
    }

    private fun observeMoneySentAndReceived() {
        personalDetailViewModel.moneySent.observe(viewLifecycleOwner, {
            binding.moneySentAmt.text = it.toString()
        })
        personalDetailViewModel.moneyReceived.observe(viewLifecycleOwner, {
            binding.moneyReceivedAmt.text = it.toString()
        })
    }

    private fun observeReceiptsOfPerson() {
        personalDetailViewModel.receipts.observe(viewLifecycleOwner, {
            adapter.receiptList = it
        })
    }
}

class PersonalDetailViewModelFactory(val database: ReceiptsDao, val application: Application, val name:String):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalDetailViewModel::class.java)) {
            return PersonalDetailViewModel(database, application, name) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
