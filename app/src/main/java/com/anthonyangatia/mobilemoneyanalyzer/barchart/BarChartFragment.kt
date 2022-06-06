package com.anthonyangatia.mobilemoneyanalyzer.linechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.BarChartFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.util.BarChartStyle
import com.anthonyangatia.mobilemoneyanalyzer.util.daysOfWeek
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.DayOfWeek
import java.time.temporal.TemporalQueries.localDate
import java.util.*

class BarChartFragment : Fragment() {

    var barEntries  = mutableListOf<BarEntry>()
    lateinit var barChart:BarChart
    private lateinit var barChartStyle: BarChartStyle

    private lateinit var binding: BarChartFragmentBinding
//    var lineEntry = ArrayList<Entry>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = BarChartFragmentBinding.inflate(inflater,container,false)

        val application = requireNotNull    (this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = BarChartViewModelFactory(dataSource, application)
        val barChartViewModel =ViewModelProvider(this, viewModelFactory).get(
            BarChartViewModel::class.java)

        barChart = binding.barChart
        barChartStyle = context?.let { BarChartStyle(it) }!!

        barChartViewModel.amtTransactedWeekLive.observe(viewLifecycleOwner) {
            if (it.size == 7) {
                var dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
                for (i in 6 downTo 0) {
                    if (dayOfWeek < 0) dayOfWeek = 6
                    var dayStr = dayOfWeekStr(dayOfWeek)
                    var amt = it.get(dayStr)
//                    Timber.i(it.values.toString())
                    var expenses = amt?.amountSentTotal
                    if (expenses == null) {
                        expenses = 0.0
                    }
//                    Timber.i(expenses.toString())
                    barEntries.add(
                        BarEntry(
                            i.toFloat(),
                            expenses.toFloat()
                        )
                    )//xaxislabel, yaxisvalue
                    dayOfWeek--
                }

                drawChart()
            }
        }

        return binding.root
    }
    class MyXAxisFormatter : ValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val dayInt = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val days = arrayOf("Su", "Mo", "Tu", "Wed", "Th", "Fr", "Sa")
            for (i in 0..6){
                val reshuffledDays = days[i]
            }
            return days.getOrNull(value.toInt()) ?: value.toString()
        }

    }

    private fun drawChart() {


        val dataSet = BarDataSet(barEntries, "label1")
        var barData = BarData(dataSet)
        barChart.data = barData
        barChartStyle.styleChart(barChart)
        barChartStyle.styleDataSet(dataSet)
        barChart.xAxis.valueFormatter = MyXAxisFormatter()
        barChart.invalidate()

    }

    fun dayOfWeekStr(index:Int):String{
        return daysOfWeek[index]
    }
}
