package com.anthonyangatia.mobilemoneyanalyzer.compare

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.databinding.CompareFragmentBinding

class CompareFragment : Fragment() {

    private lateinit var viewModel: CompareViewModel
    private lateinit var binding:CompareFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CompareFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CompareViewModel::class.java)


        return binding.root
    }


}