package com.anthonyangatia.mobilemoneyanalyzer.ui.settings

import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.anthonyangatia.mobilemoneyanalyzer.R
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDao
import com.anthonyangatia.mobilemoneyanalyzer.database.ReceiptsDatabase
import com.anthonyangatia.mobilemoneyanalyzer.databinding.FragmentSettingsBinding
import android.os.Environment
import androidx.core.content.FileProvider
import com.anthonyangatia.mobilemoneyanalyzer.BuildConfig
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.lang.Exception


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = ReceiptsDatabase.getInstance(application).receiptsDao
        val viewModelFactory = SettingsViewModelFactory(dataSource,application)
        settingsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

//        settingsViewModel.navigateToExport.observe(viewLifecycleOwner,{
//            it?.let{
//                if (it){
//                    //Navigateorexport
//                        exportData()
//                    Toast.makeText(activity, "Implement Export receipts",Toast.LENGTH_SHORT).show()
//                    settingsViewModel.onNavigationComplete()
//                }
//            }
//        })
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.compareCardView.setOnClickListener{ view->
            view.findNavController().navigate(R.id.action_navigation_settings_to_compareFragment)

        }
        binding.setTargetCardView.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_settings_to_personListFragment)
        }
        binding.classifyCardView.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_settings_to_navigation_search)
        }
        binding.textView16.setOnClickListener {
            Timber.i("Exports Clicked")
//            settingsViewModel.onExportClicked()
            settingsViewModel.receipts.observe(viewLifecycleOwner, {
                it?.let{
                    Timber.i("Size: "+ it.size)
                    exportData(it)
//                    it.forEach {
//                        Timber.i(it.message)
//                    }
                }

            })
        }



//        binding.settingsList.adapter = personsAdapter
//
//        settingsViewModel.person.observe(viewLifecycleOwner, {
//            it?.let{
//                personsAdapter.personList = it
////                Timber.i(it.toString())
//            }
//        })
//
//        val searchView = binding.settingsSearchView
//        searchView.isSubmitButtonEnabled = true
//        searchView.onQueryTextChanged{
//            searchDatabase(it)
//        }


        return root
    }

    private fun exportData(data:List<Receipt>) {
        val csvFile = context?.let {context-> generateFile(context, getCSVFileName() ) }
        if (csvFile != null) {
            exportReceipts(csvFile,data)
        }

    }
    fun goToFileIntent(context: Context, file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
//      /data/user/0/com.anthonyangatia.mobilemoneyanalyzer/files/189.csv
//        val contentUri: Uri = "content://com.anthonyangatia.mobilemoneyanalyzer.fileprovider/myimages/default_image.jpg\n"
        val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        val mimeType = context.contentResolver.getType(contentUri)
        intent.setDataAndType(contentUri, mimeType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        return intent
    }
    fun exportReceipts(csvFile: File, data:List<Receipt>) {
        csvWriter().open(csvFile) {
            writeRow(listOf("receipt_message"))//Header of csv file
            data.forEach {
                Timber.i("At writeRow")
                writeRow(listOf(it.message))
            }
            val intent = goToFileIntent(requireContext(), csvFile )
            Timber.i("Called the intent")
            startActivity(intent)
            // Header
//            writeRow(listOf("[id]", "[${Movie.TABLE_NAME}]", "[${Director.TABLE_NAME}]"))
//            receiptsList.forEachIndexed { index, movie ->
//                val directorName: String = moviesViewModel.directorsList.value?.find { it.id == movie.directorId }?.fullName ?: ""
//                writeRow(listOf(index, movie.title, directorName))
//            }
        }
    }

    private fun getCSVFileName(): String {
         val rnds = (0..202).random()
        return "Receipts $rnds.csv"
//        return "receipts.csv"
    }

    private fun generateFile(context: Context, fileName: String):File? {

        val csvFile = File(context.filesDir, fileName)
        csvFile.createNewFile()
        Timber.i("Get Path: "+csvFile.path)
        Timber.i(csvFile.toString())

        return if(csvFile.exists()) csvFile else null
    }

}

class SettingsViewModelFactory(val dataSource: ReceiptsDao, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}