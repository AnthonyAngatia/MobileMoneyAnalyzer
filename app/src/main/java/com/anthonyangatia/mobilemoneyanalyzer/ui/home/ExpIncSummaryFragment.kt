package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.databinding.ExpIncSummaryFragmentBinding
import timber.log.Timber

class ExpIncSummaryFragment : Fragment() {

    private val viewModel: ExpIncSummaryViewModel by lazy { ViewModelProvider(this ).get(ExpIncSummaryViewModel::class.java) }
    private lateinit var binding:ExpIncSummaryFragmentBinding
    val expAdapter = ExpIncAdapter(false)
    val incAdapter = ExpIncAdapter(true)
    val summaryAdapter = TransSummaryAdapter()
//    private val adapter = ReceiptsAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ExpIncSummaryFragmentBinding.inflate(inflater, container, false)

        binding.recyclerViewExpense.adapter = expAdapter
        binding.recyclerViewIncome.adapter = incAdapter
        binding.recyclerViewSummary.adapter = summaryAdapter

        viewModel.incomeListLiveData.observe(viewLifecycleOwner, {
            it?.let {
//                Timber.i("Income list"+it.size.toString())
                incAdapter.pATList = it
            }
        })
        viewModel.expenseListLiveData.observe(viewLifecycleOwner, {
            it?.let {
//                Timber.i("Expense list"+it.size.toString())
                expAdapter.pATList = it
            }
        })
        viewModel.transSummaryLiveData.observe(viewLifecycleOwner, {
            it?.let {
//                Timber.i("adapter initialized: " +it.size.toString())
                summaryAdapter.transSummaryList = it
            }

        })
        return binding.root
    }
}
