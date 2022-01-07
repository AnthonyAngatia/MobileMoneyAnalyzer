package com.anthonyangatia.mobilemoneyanalyzer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.ui.search.SearchFragmentDirections
import com.anthonyangatia.mobilemoneyanalyzer.ui.settings.target.PersonDetailFragmentDirections

class ReceiptsAdapter(private val className:String? = null): RecyclerView.Adapter<ReceiptsAdapter.ViewHolder>() {
    var receiptList = listOf<Receipt>()
        set(value) {
            field =value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view_sms_receipt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = receiptList[position]
        holder.arrow.setImageResource(when(item.amountSent){
            null -> R.drawable.rotated_green
            else -> R.drawable.red_arrow_svg
        })
        if(className == "SearchFragment"){
            holder.itemView.setOnClickListener {
                it.findNavController().navigate(SearchFragmentDirections.actionNavigationSearchToClassifyFragment(item.receiptId))
            }

        }else{
            holder.itemView.setOnClickListener {
                it.findNavController().navigate(PersonDetailFragmentDirections.actionPersonDetailFragmentToClassifyFragment(item.receiptId))
            }
        }


        if(item.transactionType == "sentToNumber"){
            holder.amt.text = "Kshs "+item.amountSent.toString()
            holder.personBusinessName.text = item.name
        }else if (item.transactionType == "sentToBuyGoods"){
            holder.amt.text = "Kshs "+item.amountSent.toString()
            holder.personBusinessName.text = item.name
        }else if(item.transactionType == "sentToPayBill"){
            holder.amt.text = "Kshs "+item.amountSent.toString()
            holder.personBusinessName.text = item.name
        } else if(item.transactionType == "sentToMshwari") {
            holder.amt.text = "Kshs "+item.amountSent.toString()
            holder.personBusinessName.text = "MSHWARI"
        }else if(item.transactionType == "receivedMoney") {
            holder.amt.text = "Kshs "+item.amountReceived.toString()
            holder.personBusinessName.text = item.name
        }else if(item.transactionType == "receivedMoneyMshwari") {
            holder.amt.text = "Kshs "+item.amountReceived.toString()
            holder.personBusinessName.text = "MSHWARI"
        }else if(item.transactionType == "accountBalance") {
            holder.amt.text = "Kshs "+item.balance.toString()
            holder.personBusinessName.text = "Account Balance"
        }else if(item.transactionType == "sentToBuyAirTime") {
            holder.amt.text = "Kshs "+item.amountReceived.toString()
            holder.personBusinessName.text = "AIRTIME"
        }
//        holder.phoneNumber.text = "0791278088"
    }

    override fun getItemCount() = receiptList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //        val smsMessage: TextView = itemView.findViewById(R.id.messageTextView)
        val personBusinessName: TextView = itemView.findViewById(R.id.nameTextView)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNoTextView)//TODO:1 Update the regex
        val amt: TextView = itemView.findViewById(R.id.amountTextView)
        val arrow: ImageView = itemView.findViewById(R.id.arrowImage)


    }


}