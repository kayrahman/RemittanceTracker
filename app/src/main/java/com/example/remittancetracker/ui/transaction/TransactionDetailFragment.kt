package com.example.remittancetracker.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.databinding.TotalCashInNOutDetailFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionDetailFragment : BaseFragment() {

    private val viewModel: TransactionViewModel by viewModel()
    private lateinit var binding : TotalCashInNOutDetailFragmentBinding

    override val _viewModel: BaseViewModel
        get() = viewModel

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TotalCashInNOutDetailFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        binding.lifecycleOwner = this
    }

}