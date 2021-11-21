package com.anthonyangatia.mobilemoneyanalyzer

import android.content.ContentResolver
import android.graphics.Color.red
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.MonthlyChartFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.*
import kotlin.collections.ArrayList

class MonthlyChartFragment : Fragment() {

    private var _binding: MonthlyChartFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MonthlyChartViewModel
    private lateinit var resolver: ContentResolver
    var listOfReceipts = listOf<Receipt>()
    var lineEntry = ArrayList<Entry>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = MonthlyChartFragmentBinding.inflate(inflater,container,false)

//TODO: edit the parameters of the viewmodel
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = MonthlyChartViewModelFactory(dataSource, application)
        val monthlyChartViewModel =ViewModelProvider(this, viewModelFactory).get(MonthlyChartViewModel::class.java)
        binding.monthlyChartFragment = monthlyChartViewModel
        monthlyChartViewModel.amountTransactedListLiveData.observe(viewLifecycleOwner, {
            lineEntry.clear()
            for(index in it.indices){
                var amountSentTotal = 0.0
                if(it[index].amountSentTotal == null){
                     amountSentTotal = 0.0
                }else{
                     amountSentTotal = it[index].amountSentTotal!!
                }

                Log.d(javaClass.simpleName, "Loop "+index+"AmountSent "+ amountSentTotal)
                lineEntry.add(Entry(index.toFloat(), amountSentTotal.toFloat()))
                binding.monthlyReceipts.text = it[index].toString() + index
            }
            Log.d(javaClass.simpleName, "Out loop")

            drawGraph()
        })
        return binding.root
    }

    fun drawGraph(){
        val lineDataSet = LineDataSet(lineEntry, "First")
        val data = LineData(lineDataSet )
        binding.chartId.data = data
        binding.chartId.invalidate()

    }
}