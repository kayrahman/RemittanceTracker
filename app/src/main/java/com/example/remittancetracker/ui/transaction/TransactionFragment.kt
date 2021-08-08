package com.example.remittancetracker.ui.transaction

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.remittancetracker.R
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.databinding.TransactionFragmentBinding
import com.nkr.bazaranocustomer.base.NavigationCommand
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : BaseFragment() {

    private val viewModel: TransactionViewModel by inject()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding: TransactionFragmentBinding
    private val safeArgs: TransactionFragmentArgs by navArgs()

    private var userType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transaction_type = safeArgs.transactionType
        userType = safeArgs.userType
        viewModel.setTransactionType(transaction_type)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListener()
    }

    private fun setupListener() {
        binding.btnBank.setOnClickListener {
            navigateToConfirmTransactionScreen(getString(R.string.label_bank))
        }

        binding.btnBkash.setOnClickListener {
            navigateToConfirmTransactionScreen(getString(R.string.label_bkash))
        }
    }

    private fun validateInfo(): Boolean {
        val amount = binding.etAmount.text.trim().toString()
        val city = binding.etCity.text.trim().toString()
        return try {
            val amount_num = Integer.parseInt(amount as String)
            if (amount_num != 0) {
                true
            } else {
                viewModel.showSnackBar.value = "Amount can not be 0"
                false
            }
        } catch (e: Exception) {
            viewModel.showSnackBar.value = "Error Occured"
            false
        }


    }


    fun navigateToConfirmTransactionScreen(payment_type: String) {
        if (validateInfo()) {
            val actionConfirm =
                TransactionFragmentDirections.actionTransactionFragmentToConfirmTransactionFragment(
                    payment_type,userType
                )
            viewModel.navigationCommand.value = NavigationCommand.To(actionConfirm)
        }else{

        }

    }

}