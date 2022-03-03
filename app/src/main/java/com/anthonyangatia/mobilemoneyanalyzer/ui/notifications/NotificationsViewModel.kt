package com.anthonyangatia.mobilemoneyanalyzer.ui.notifications

import android.app.Application
import androidx.lifecycle.*
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.database.Target
import com.anthonyangatia.mobilemoneyanalyzer.util.AmountTransacted
import com.anthonyangatia.mobilemoneyanalyzer.util.CategoryAmount
import kotlinx.coroutines.launch
import timber.log.Timber

class NotificationsViewModel (application: Application): AndroidViewModel(application) {
    val catAmt: ArrayList<CategoryAmount> = ArrayList()
    val database = ReceiptsDatabase.getInstance(application).receiptsDao
    var targets: LiveData<List<Target>> = database.getAllTargets()
    var categories:List<String?> = mutableListOf()
    var categoriesAndAmount:MutableLiveData<List<CategoryAmount>> = MutableLiveData()


    init {
        allCategories()
    }
    fun addCategory(categoryAmount: CategoryAmount){
        catAmt.add(categoryAmount)
        categoriesAndAmount.value = catAmt //Trigger a change in the live data as we add a new item in the list

    }

    fun allCategories(){
        Timber.i("Allcategories called")
        viewModelScope.launch {
            categories = database.getAllCategories()
//            Timber.i(categories.get(0).toString())
            Timber.i("Category size is :" +categories.size)
            //Get Category and amount
            if(categories.size != 0){
                for (category in categories){
                    category?.let{
                        val catAmt = CategoryAmount(category, database.getCategoryAndAmount(category))
                        addCategory(catAmt)
                    }

                }
            }else{
                Timber.i("Category size is empty:" +categories.size)
            }
        }
    }



}