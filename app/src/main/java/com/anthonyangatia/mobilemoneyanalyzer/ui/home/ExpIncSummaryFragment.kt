package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.databinding.ExpIncSummaryFragmentBinding
import com.anthonyangatia.mobilemoneyanalyzer.util.PersonAmountTransacted

class ExpIncSummaryFragment : Fragment() {

    private lateinit var viewModel: ExpIncSummaryViewModel
    private lateinit var binding:ExpIncSummaryFragmentBinding
    val expAdapter = ExpIncAdapter(false)
    val incAdapter = ExpIncAdapter(true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpIncSummaryFragmentBinding.inflate(inflater, container, false)

        binding.recyclerViewExpense.adapter = expAdapter
        binding.recyclerViewIncome.adapter = incAdapter

        val application = requireNotNull    (this.activity).application
        val viewModelFactory = ExpIncSummaryViewModelFactory( application)
        viewModel =ViewModelProvider(this, viewModelFactory).get(ExpIncSummaryViewModel::class.java)

        viewModel.incomeListLiveData.observe(viewLifecycleOwner, {
            it?.let {
                incAdapter.pATList = it
            }
        })
        viewModel.expenseListLiveData.observe(viewLifecycleOwner, {
            it?.let {
                expAdapter.pATList = it
            }
        })
        return binding.root
    }



}

class ExpIncSummaryViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpIncSummaryViewModel::class.java)) {
            return ExpIncSummaryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
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
        holder.phoneNumber.text = item.personAndBusiness.phoneNumber
        if(income){
            holder.amt.text = item.amountTransacted.amountReceivedTotal.toString()
        }else{
            holder.amt.text = item.amountTransacted.amountSentTotal.toString()
        }

        holder.name.text = item.personAndBusiness.name
    }

    override fun getItemCount() = pATList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nameEI)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNoEI)//TODO:1 Update the regex
        val amt: TextView = itemView.findViewById(R.id.amtTransacted)


    }

}
