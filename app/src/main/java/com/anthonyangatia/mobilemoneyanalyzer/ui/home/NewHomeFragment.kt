package com.anthonyangatia.mobilemoneyanalyzer.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentNewHomeBinding


class NewHomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        var binding = FragmentNewHomeBinding.inflate(inflater, container, false)


        return binding.root
    }



}