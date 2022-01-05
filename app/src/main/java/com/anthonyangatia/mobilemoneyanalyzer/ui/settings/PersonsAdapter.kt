package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.database.Person
import com.anthonyangatia.mobilemoneyanalyzer.databinding.ItemViewPersonBinding
import timber.log.Timber


class PersonsAdapter(private val clickListener: PersonListener): ListAdapter< Person,PersonsAdapter.ViewHolder>(PersonDiffCallback()){
//    var personList = listOf<Person>()
//        set(value) {
//            field =value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

//    override fun getItemCount() = personList.size


    class ViewHolder(val binding: ItemViewPersonBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Person, clickListener: PersonListener) {
            binding.clickListener = clickListener
            item.let {
                binding.settingsNameTv.text = it.name
                binding.settingsPhoneNo.text = it.phoneNumber
            }
             //TODO:1 Update the regex
        }

//        private fun ViewHolder.bind(item: Person) { }

        companion object {
             fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_view_person, parent, false)
                 val binding = ItemViewPersonBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
class PersonDiffCallback : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.phoneNumber == newItem.phoneNumber
    }

    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem == newItem
    }

}
class PersonListener(val clickListener: (phoneNo: String) -> Unit) {
    fun onClick(person:Person?) {
        Timber.i(person.toString())
        person?.let {
            clickListener(person.phoneNumber)
        }
    }

}
//class PersonListener(val clickListener: (phoneNo: String) -> Unit) {
//    fun onClick(person: Person) = clickListener(person.phoneNumber)
//}