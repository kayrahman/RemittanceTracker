package com.example.remittancetracker.ui.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.databinding.TotalCashInNOutDetailFragmentBinding
import com.example.remittancetracker.ui.home.TransactionListAdapter
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_IN
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_OUT
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionDetailFragment : BaseFragment() {

    private val viewModel: TransactionViewModel by viewModel()
    private lateinit var binding : TotalCashInNOutDetailFragmentBinding

    override val _viewModel: BaseViewModel
        get() = viewModel

    private val safeArgs : TransactionDetailFragmentArgs by navArgs()
    private var trns_detail_type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         trns_detail_type = safeArgs.transactionType
        viewModel.setTransactionDetailType(trns_detail_type)
    }

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TotalCashInNOutDetailFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        when (trns_detail_type) {
            TYPE_TOTAL_CASH_IN -> {
                viewModel.getTransactions(TYPE_TOTAL_CASH_IN)
            }
            TYPE_TOTAL_CASH_OUT -> {
                viewModel.getTransactions(TYPE_TOTAL_CASH_OUT)
            }
            else -> {
                viewModel.showSnackBar.value = "Something went Wrong"
            }
        }

        setupAdapter()

    }

    private fun setupAdapter() {
       val adapter = TransactionListAdapter()
        binding.rvTransactionDetail.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactionDetail.adapter = adapter

        viewModel.transactionList.observe(viewLifecycleOwner, Observer {
            Log.i("transaction_detail","observed : ${it.size.toString()}")
            adapter.submitList(it)

        })

    }


}