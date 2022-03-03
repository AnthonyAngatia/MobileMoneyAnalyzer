package com.anthonyangatia.mobilemoneyanalyzer.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentNotificationsBinding
import com.anthonyangatia.mobilemoneyanalyzer.ui.home.CategoryAdapter
import timber.log.Timber

class NotificationsFragment : Fragment() {

    private val notificationsViewModel: NotificationsViewModel by lazy { ViewModelProvider(this).get(NotificationsViewModel::class.java)  }
    private lateinit var binding: FragmentNotificationsBinding
    val categoryAdapter = CategoryAdapter()
    val targetAdapter = TargetAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        binding.categoryRecyclerView.adapter = categoryAdapter

        notificationsViewModel.categoriesAndAmount.observe(viewLifecycleOwner, {
            categoryAdapter.categoryAmountList = it
        })

        binding.recyclerViewTargets.adapter = targetAdapter

        notificationsViewModel.targets.observe(viewLifecycleOwner, {
            Timber.i("Observing targets:" + it.size.toString())
            it?.let{
                targetAdapter.targetList = it
            }
        })



//
        return binding.root
    }


}