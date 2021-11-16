package com.anthonyangatia.mobilemoneyanalyzer

import android.content.ContentResolver
import android.graphics.Color.red
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentSmsReceiptsBinding
import com.anthonyangatia.mobilemoneyanalyzer.databinding.MonthlyChartFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MonthlyChartFragment : Fragment() {

    private var _binding: MonthlyChartFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MonthlyChartViewModel
    private lateinit var resolver: ContentResolver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = MonthlyChartFragmentBinding.inflate(inflater,container,false)

        drawGraph()

        return binding.root
    }

    fun drawGraph(){
        val xvalues = listOf<String>("value1", "value2", "xvalue3")

        var lineEntry = ArrayList<Entry>();
        lineEntry.add(Entry(20f, 5F))
        lineEntry.add(Entry(30f, 1F))
        lineEntry.add(Entry(40f, 2F))
        lineEntry.add(Entry(50f, 3F))
        lineEntry.add(Entry(60f, 3F))
        lineEntry.add(Entry(70f, 1F))


        val lineDataSet = LineDataSet(lineEntry, "First")
        lineDataSet.color = resources.getColor(R.color.purple_700)
        val lineChart: LineChart = LineChart(context)
//        lineChart.axisRight.isEnabled = false
        lineChart.apply {
            axisRight.isEnabled = false
            background = null
        }
        lineChart.xAxis.apply {
            axisMinimum = -1f
            axisMaximum = 100f
            isGranularityEnabled = true
            granularity = 5f //0 5 10 15 etc
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
        }
        lineChart.setTouchEnabled(true)
        lineChart.description = null
        lineChart.legend.isEnabled = false

        val data = LineData(lineDataSet )
        binding.chartId.data = data
        binding.chartId.invalidate()


    }


}