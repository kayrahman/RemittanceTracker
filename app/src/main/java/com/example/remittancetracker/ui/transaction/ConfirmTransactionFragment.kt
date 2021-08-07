package com.example.remittancetracker.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.remittancetracker.R
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.databinding.FragmentConfirmTransactionBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmTransactionFragment : BaseFragment() {

    private val viewModel: TransactionViewModel by inject()
    override val _viewModel: BaseViewModel
        get() = viewModel


    private lateinit var binding : FragmentConfirmTransactionBinding
    private val safeArgs : ConfirmTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val payment_method = safeArgs.paymentType
        viewModel.setPaymentMethodType(payment_method)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmTransactionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnTransaction.setOnClickListener {
            viewModel.postTransactionInfo()
        }
    }





}