package com.anthonyangatia.mobilemoneyanalyzer.util

import androidx.appcompat.widget.SearchView

inline fun  SearchView.onQueryTextChanged( crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }
        override fun onQueryTextChange(query: String?): Boolean {
            if(query != null){
                listener(query)
            }
            return true
        }

    })
}