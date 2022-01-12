package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.TransactionSummary
import com.anthonyangatia.mobilemoneyanalyzer.util.PersonAmountTransacted
import timber.log.Timber
import java.util.*

//If state
class ExpIncAdapter(val income:Boolean): RecyclerView.Adapter<ExpIncAdapter.ViewHolder>() {
    var pATList: List<PersonAmountTransacted> = listOf()
        set(value) {
            field =value
            notifyDataSetChanged()
        }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_exp_inc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //All this will generate a bug
        val item = pATList[position]
        holder.name.text = item.personAndBusiness.name
        holder.phoneNumber.text = item.personAndBusiness.phoneNumber ?: ""
        if(income){
            holder.amt.text = item.amountTransacted.amountReceivedTotal.toString()
        }else{
            holder.amt.text = item.amountTransacted.amountSentTotal.toString()
        }
    }

    override fun getItemCount() = pATList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nameEI)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNoEI)//TODO:1 Update the regex
        val amt: TextView = itemView.findViewById(R.id.amtTransacted)


    }

}

class TransSummaryAdapter(): RecyclerView.Adapter<TransSummaryAdapter.ViewHolder>() {
    var transSummaryList:List<TransactionSummary>  = listOf()
        set(value) {
            field =value
            notifyDataSetChanged()
        }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_transaction_summary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //All this will generate a bug
        val item = transSummaryList[position]
        holder.transactionType.text = item.transactionType
//        Timber.i("Bind holder:"+ position.toString())
        holder.amount.text = item.amount.toString()
    }

    override fun getItemCount() = transSummaryList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val transactionType: TextView = itemView.findViewById(R.id.transactionType)
        val amount: TextView = itemView.findViewById(R.id.amtTransSum)

    }

}