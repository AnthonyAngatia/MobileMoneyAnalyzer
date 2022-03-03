package com.anthonyangatia.mobilemoneyanalyzer.linechart

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.MonthlyChartFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.util.LineChartStyle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.collections.ArrayList

class MonthlyChartFragment : Fragment() {

    private lateinit var binding: MonthlyChartFragmentBinding
    var lineEntry = ArrayList<Entry>()

//    @Inject
    lateinit var chartStyle: LineChartStyle

    lateinit var lineChart:LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MonthlyChartFragmentBinding.inflate(inflater,container,false)

//TODO: edit the parameters of the viewmodel
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = MonthlyChartViewModelFactory(dataSource, application)
        val monthlyChartViewModel =ViewModelProvider(this, viewModelFactory).get(
            MonthlyChartViewModel::class.java)
//        binding.monthlyChartFragment = monthlyChartViewModel
        lineChart = binding.chartId
        chartStyle = context?.let { LineChartStyle(it) }!!
        chartStyle.styleChart(lineChart)
        monthlyChartViewModel.amountTransactedListLiveData.observe(viewLifecycleOwner, {
            lineEntry.clear()
            for(index in it.indices){
                var amountSentTotal: Double?
                if(it[index].amountSentTotal == null){
                     amountSentTotal = 0.0
                }else{
                     amountSentTotal = it[index].amountSentTotal!!
                }

//                Log.d(javaClass.simpleName, "Loop "+index+"AmountSent "+ amountSentTotal)
                lineEntry.add(Entry(index.toFloat(), amountSentTotal.toFloat()))
                binding.monthlyReceipts.text = it[index].toString() + index
            }

            drawGraph()
        })
        return binding.root
    }

    fun drawGraph(){
        val lineDataSet = LineDataSet(lineEntry, "First")
        chartStyle.styleLineDataSet(lineDataSet)
//        lineDataSet.lineWidth = 1.25f
        lineDataSet.color = Color.CYAN
        lineDataSet.setCircleColor(Color.RED)
        val data = LineData(lineDataSet )
        chartStyle.styleChart(lineChart)
        lineChart.data = data
        lineChart.invalidate()

    }
}