package com.anthonyangatia.mobilemoneyanalyzer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt

class ReceiptsAdapter: RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>() {
    var receiptList = listOf<Receipt>()
        set(value) {
            field =value
        }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.sms_receipt_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = receiptList[position].code
        holder.smsMessage.text = item

    }

    override fun getItemCount() = receiptList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val smsMessage: TextView = itemView.findViewById(R.id.messageTextView)


    }


}