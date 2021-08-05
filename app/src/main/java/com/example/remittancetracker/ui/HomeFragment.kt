package com.example.remittancetracker.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.remittancetracker.R
import com.example.remittancetracker.databinding.HomeFragmentBinding
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_IN
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_OUT
import com.example.remittancetracker.util.TYPE_TRANSACTION_RECEIVE_MONEY
import com.example.remittancetracker.util.TYPE_TRANSACTION_SEND_MONEY
import com.nkr.bazaranocustomer.base.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this

        setupListener()

    }

    private fun setupListener() {
        binding.cvTotalCashIn.setOnClickListener {
            navigateToTransactionDetail(TYPE_TOTAL_CASH_IN)
        }
        binding.cvTotalCashOut.setOnClickListener {
            navigateToTransactionDetail(TYPE_TOTAL_CASH_OUT)
        }


        binding.btnSendMoney.setOnClickListener {
            navigateToTransactionScreen(TYPE_TRANSACTION_SEND_MONEY)
        }

        binding.btnRcvMoney.setOnClickListener {
            navigateToTransactionScreen(TYPE_TRANSACTION_RECEIVE_MONEY)
        }


    }



    fun navigateToTransactionScreen(transaction_type : String){
        val actionTransDetail = HomeFragmentDirections.actionHomeFragmentToTransactionFragment(transaction_type)
        viewModel.navigationCommand.value = NavigationCommand.To(actionTransDetail)
    }

    fun navigateToTransactionDetail(transaction_detail_type : String){
        val actionTransDetail = HomeFragmentDirections.actionHomeFragmentToTotalCashInNOutDetailFragment(transaction_detail_type)
        viewModel.navigationCommand.value = NavigationCommand.To(actionTransDetail)
    }

}