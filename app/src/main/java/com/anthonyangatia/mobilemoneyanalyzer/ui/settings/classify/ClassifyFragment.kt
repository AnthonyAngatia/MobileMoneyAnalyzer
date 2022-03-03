package com.anthonyangatia.mobilemoneyanalyzer.ui.settings.classify

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.anthonyangatia.mobilemoneyanalyzer.databinding.ClassifyFragmentBinding
import timber.log.Timber

class ClassifyFragment : Fragment() {
    private lateinit var viewModel: ClassifyViewModel
    private lateinit var binding: ClassifyFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ClassifyFragmentBinding.inflate(inflater,container, false)
        val application = requireNotNull(this.activity).application
        val args = ClassifyFragmentArgs.fromBundle(requireArguments())
        val receiptId = args.receiptId
        Timber.i(receiptId.toString())
        val viewModelFactory = ClassifyViewModelFactory(application, receiptId)

        viewModel = ViewModelProvider(this,viewModelFactory).get(ClassifyViewModel::class.java)

        viewModel.receipt.observe(viewLifecycleOwner,{
            it?.let {
                binding.message.text = it.message
            }
        })

        binding.classifyButton.setOnClickListener {
            val category = binding.classEditText.text.toString()
            if (category != null) {
                viewModel.classifyReceipt(category)
                Toast.makeText(context,"Category field is empty", Toast.LENGTH_SHORT).show()
                Toast.makeText(context,"Category saved", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Category field is empty", Toast.LENGTH_SHORT).show()

            }
        }

        return binding.root
    }


}
class ClassifyViewModelFactory( val application: Application, val receiptId:Long):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassifyViewModel::class.java)) {
            return ClassifyViewModel( application, receiptId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}