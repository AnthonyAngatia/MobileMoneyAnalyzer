package com.anthonyangatia.mobilemoneyanalyzer

import android.content.ContentResolver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase

import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentSmsReceiptsBinding


class ReceiptsFragment : Fragment() {
    private var _binding:FragmentSmsReceiptsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReceiptViewModel
    private lateinit var resolver: ContentResolver
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSmsReceiptsBinding.inflate(inflater,container,false)

        val adapter = ReceiptsAdapter()
        binding.smsReceiptList.adapter = adapter

        resolver = activity?.contentResolver!!

        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = ReceiptsViewModelFactory(dataSource, application)
        val receiptViewModel =ViewModelProvider(this, viewModelFactory).get(ReceiptViewModel::class.java)

        receiptViewModel.receipts?.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.receiptList = it
            }
        })
//        if(receiptViewModel.receipts == null){
//            adapter.receiptList = listOf(Receipt(), Receipt())
//        }else{
//            adapter.receiptList = receiptViewModel.receipts!!
//        }
        return binding.root
    }



}