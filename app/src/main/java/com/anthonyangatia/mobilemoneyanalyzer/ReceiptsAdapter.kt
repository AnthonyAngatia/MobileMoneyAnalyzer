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
            notifyDataSetChanged()
        }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.sms_receipt_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = receiptList[position]
        if(item.transactionType == "balance"){
            holder.recipientName.text = "MPESA BALANCE"
            holder.amt.text = "Kshs "+item.balance.toString()
//            TODO:Rename
        }else if (item.transactionType == "sent"){
            holder.amt.text = "Kshs "+item.amountSent.toString()
            holder.recipientName.text = item.recipient

        }

        else if(item.transactionType == "received") {
            holder.amt.text = "Kshs "+item.amountReceived.toString()
            holder.recipientName.text = item.sender
        }
//        holder.phoneNumber.text = "0791278088"




    }

    override fun getItemCount() = receiptList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val smsMessage: TextView = itemView.findViewById(R.id.messageTextView)
        val recipientName = itemView.findViewById<TextView>(R.id.nameTextView)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNoTextView)//TODO:1 Update the regex
        val amt: TextView = itemView.findViewById(R.id.amountTextView)


    }


}