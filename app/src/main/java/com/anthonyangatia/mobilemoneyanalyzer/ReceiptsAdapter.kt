package com.anthonyangatia.mobilemoneyanalyzer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReceiptsAdapter: RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>() {
    var smsList = listOf("mmmm1", "Kshs 140 paid to SPRINGS MZIMA", "mmmm3", "Kshs 140 paid to SPRINGS MZIMA", "mmmm2", "mmmm3","Kshs 140 paid to SPRINGS MZIMA", "mmmm2", "Kshs 140 paid to SPRINGS MZIMA", "mmmm1", "Kshs 140 paid to SPRINGS MZIMA", "mmmm3")




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.sms_receipt_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = smsList[position]
        holder.smsMessage.text = item

    }

    override fun getItemCount() = smsList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val smsMessage: TextView = itemView.findViewById(R.id.messageTextView)


    }


}