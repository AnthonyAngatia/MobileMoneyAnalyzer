package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.databinding.ItemViewPersonBinding
import com.anthonyangatia.mobilemoneyanalyzer.ui.settings.compare.CompareFragment
import com.anthonyangatia.mobilemoneyanalyzer.ui.settings.compare.CompareViewModel
import timber.log.Timber


class PersonsAdapter(private val clickListener: PersonListener, val from: String, val viewModel:CompareViewModel?=null): ListAdapter< PersonAndBusiness,PersonsAdapter.ViewHolder>(PersonDiffCallback()){
//    var personList = listOf<Person>()
//        set(value) {
//            field =value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val personItem = getItem(position)

        holder.bind(personItem,clickListener)

        holder.itemView.setOnClickListener {
            if (from == "CompareFragment"){
                if(viewModel?.isPersonASet == false){
                    Timber.i("reaching the ifs")
                    viewModel.getPersonA(personItem.name)
                }else if(viewModel?.isPersonBSet == false){
                    viewModel.getPersonB(personItem.name)
                }else{
                    Timber.i("More than 2 selected")
                }
            }else if(from == "PersonListFragment") {
                it.findNavController().navigate(PersonListFragmentDirections.actionPersonListFragmentToPersonDetailFragment(personItem.name))
            }
//            clickListener.onClick(personItem)
        }
    }

//    override fun getItemCount() = personList.size


    class ViewHolder(val binding: ItemViewPersonBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: PersonAndBusiness, clickListener: PersonListener) {
            item.let {
                binding.settingsNameTv.text = it.name
                binding.settingsPhoneNo.text = it.phoneNumber
                if (it.targetExpense != null){
                    binding.targetAmtItemView.text="Target Expense  "+it.targetExpense.toString()
                }
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
class PersonDiffCallback : DiffUtil.ItemCallback<PersonAndBusiness>() {
    override fun areItemsTheSame(oldItem: PersonAndBusiness, newItem: PersonAndBusiness): Boolean {
        return oldItem.phoneNumber == newItem.phoneNumber
    }

    override fun areContentsTheSame(oldItem: PersonAndBusiness, newItem: PersonAndBusiness): Boolean {
        return oldItem == newItem
    }

}
class PersonListener(val clickListener: (personAndBusiness: PersonAndBusiness) -> Unit) {
    fun onClick(personAndBusiness:PersonAndBusiness?) {
//        Timber.i(person.toString())
        personAndBusiness?.let {
            clickListener(personAndBusiness)
        }
    }

}
//class PersonListener(val clickListener: (phoneNo: String) -> Unit) {
//    fun onClick(person: Person) = clickListener(person.phoneNumber)
//}