package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.Person


class SettingsAdapter: RecyclerView.Adapter<SettingsAdapter.ViewHolder>()  {
    var personList = listOf<Person>()
        set(value) {
            field =value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.person_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsAdapter.ViewHolder, position: Int) {
        val item = personList[position]
        holder.recipientName.text = item.name
        holder.phoneNumber.text = item.phoneNumber
    }

    override fun getItemCount() = personList.size
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recipientName = itemView.findViewById<TextView>(R.id.settingsNameTv)
        val phoneNumber: TextView = itemView.findViewById(R.id.settingsPhoneNo)//TODO:1 Update the regex



    }
}