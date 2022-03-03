package com.anthonyangatia.mobilemoneyanalyzer.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.Target
import com.anthonyangatia.mobilemoneyanalyzer.database.TransactionSummary
import com.anthonyangatia.mobilemoneyanalyzer.ui.home.TransSummaryAdapter

class TargetAdapter(): RecyclerView.Adapter<TargetAdapter.ViewHolder>() {

    var targetList:List<Target>  = listOf()
        set(value) {
            field =value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_target, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = targetList[position]
        holder.currentExpenditure.text = item.currentExpenditure.toString()
        holder.target.text = item.targetExpense.toString()

    }

    override fun getItemCount() = targetList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val target:TextView = itemView.findViewById(R.id.targetTextView)
        val currentExpenditure:TextView = itemView.findViewById(R.id.currentExpTextView)
        val message:TextView = itemView.findViewById(R.id.targetMessageTextView)
        val date:TextView = itemView.findViewById(R.id.targetDayDate)
        val time:TextView = itemView.findViewById(R.id.targetTime)


    }
}