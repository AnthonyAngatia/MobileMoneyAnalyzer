package com.anthonyangatia.mobilemoneyanalyzer.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.LineDataSet
import timber.log.Timber
import javax.inject.Inject

class LineChartStyle @Inject constructor(private val context:Context){

    fun styleChart(lineChart: LineChart){
        lineChart.apply{
            axisRight.isEnabled = false
            axisLeft.apply {
                isEnabled = true
            }
            xAxis.apply {
                axisMinimum = 1f
//                axisMaximum = 30f
                isGranularityEnabled = true
//                granularity = 4f
                setDrawGridLines(false)
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM

            }

            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setPinchZoom(false)
            description = null
            legend.isEnabled = false
        }

    }
    fun styleLineDataSet(lineDataSet: LineDataSet){
        lineDataSet.apply {
//            color = ContextCompat.getColor(context, R.color.purple_700)
//            valueTextColor = ContextCompat.getColor(context, R.color.white)
//            setDrawValues(false)
            lineWidth = 1.25f
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
//            setDrawCircles(false)
//            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
    }
}
class BarChartStyle(private val context:Context){
    fun styleChart(barChart: BarChart){
        barChart.apply {
            axisRight.isEnabled = false
//            this.setDrawBarShadow(true)

//            this.setDrawValueAboveBar(false)
//            this.isAutoScaleMinMaxEnabled
            axisLeft.apply {
                Timber.i("Range of value:${this.mAxisRange}")
                this.mAxisMaximum=7000f //? Can we find mValuesMaximum
                this.isAxisMaxCustom
                isEnabled = true
                setDrawGridLines(false)
            }
            xAxis.apply {
//                axisMinimum = 1f
//                axisMaximum = 30f
                isGranularityEnabled = true
//                granularity = 4f
                setDrawGridLines(false)
                setDrawAxisLine(true)
                position = XAxis.XAxisPosition.BOTTOM

            }


            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            description = null
            legend.isEnabled = false
        }
    }
    fun styleDataSet(dataSet: BarDataSet){
        dataSet.apply {
            color = ContextCompat.getColor(context, R.color.indigo)
        }

    }

}