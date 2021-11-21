package com.anthonyangatia.mobilemoneyanalyzer.piechart

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.PieFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import android.text.style.ForegroundColorSpan

import android.text.style.RelativeSizeSpan

import android.text.SpannableString





class PieFragment : Fragment() {

    private var _binding: PieFragmentBinding? = null
    private val binding get() = _binding!!
    var pieEntryArrayList = ArrayList<PieEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = PieFragmentBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = PieViewModelFactory(dataSource, application)
        val pieViewModel =ViewModelProvider(this, viewModelFactory).get(
            PieViewModel::class.java)

        pieViewModel.totalAmtTypeReceived.observe(viewLifecycleOwner, { it ->
            var receivedTypeTotal = it.amountReceivedTotal
            if(receivedTypeTotal == null){
                receivedTypeTotal = 0.0
            }
            pieEntryArrayList.add(PieEntry(receivedTypeTotal.toFloat(), "Received"))
            drawPieChart()
        })
        pieViewModel.totalAmtTypeSent.observe(viewLifecycleOwner, { it ->
            var sentTypeTotal = it.amountSentTotal
            if(sentTypeTotal == null){
                sentTypeTotal = 0.0
            }
            pieEntryArrayList.add(PieEntry(sentTypeTotal.toFloat(), "Sent"))
            drawPieChart()
        })


        return binding.root
    }

    fun drawPieChart(){
        val pieDataSet = PieDataSet(pieEntryArrayList, "Transaction Type" )
        pieDataSet.sliceSpace = 2f
        pieDataSet.colors = (ColorTemplate.VORDIPLOM_COLORS).toList()
        pieDataSet.valueTextSize = 12f
        val pieData = PieData(pieDataSet)
        binding.pieChartView.data = pieData
//        binding.pieChartView.centerText = generateCenterText()
        binding.pieChartView.invalidate()
    }
    private fun generateCenterText(): SpannableString? {
        val s = SpannableString("Transaction Type")
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }

}