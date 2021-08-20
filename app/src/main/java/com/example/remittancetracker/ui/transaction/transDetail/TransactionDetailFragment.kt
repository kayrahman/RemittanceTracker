package com.example.remittancetracker.ui.transaction.transDetail

import android.app.DatePickerDialog
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
import com.example.remittancetracker.ui.transaction.TransactionViewModel
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_IN
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_OUT
import com.example.remittancetracker.util.USER_TYPE_ADMIN
import com.example.remittancetracker.util.USER_TYPE_AGENT
import com.google.android.material.datepicker.MaterialDatePicker
import com.nkr.bazarano.util.Helperutils.Companion.formattedDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat

class TransactionDetailFragment : BaseFragment() {

    private val viewModel: TransactionDetailViewModel by viewModel()
    private lateinit var binding: TotalCashInNOutDetailFragmentBinding

    override val _viewModel: BaseViewModel
        get() = viewModel

    private val safeArgs: TransactionDetailFragmentArgs by navArgs()
    private var trns_detail_type = ""
    private var user_type = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TotalCashInNOutDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        trns_detail_type = safeArgs.transactionType
        user_type = safeArgs.userType
        viewModel.setTransactionDetailType(trns_detail_type)



        when (trns_detail_type) {
            TYPE_TOTAL_CASH_IN -> {

                if(user_type == USER_TYPE_ADMIN) {
                    Log.i("trans_detail_type","${trns_detail_type.toString()}")
                    viewModel.getTransactions(USER_TYPE_ADMIN,TYPE_TOTAL_CASH_IN)
                }

                if(user_type == USER_TYPE_AGENT) {
                    viewModel.getTransactions( USER_TYPE_AGENT,TYPE_TOTAL_CASH_IN)
                }
            }
            TYPE_TOTAL_CASH_OUT -> {

                if(user_type == USER_TYPE_AGENT) {
                    viewModel.getTransactions( USER_TYPE_AGENT,TYPE_TOTAL_CASH_OUT)
                }

                if(user_type == USER_TYPE_ADMIN) {
                    Log.i("trans_detail_type","${trns_detail_type.toString()}")
                    viewModel.getTransactions(USER_TYPE_ADMIN,TYPE_TOTAL_CASH_OUT)
                }

            }
            else -> {
                viewModel.showSnackBar.value = "Something went Wrong"
            }
        }

        setupAdapter()
        setupListener()

    }

    private fun setupListener() {
        binding.btnFrom.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()
            datePicker.show(childFragmentManager,"from_tag")

            datePicker.addOnPositiveButtonClickListener {
                binding.btnFrom.text = datePicker.headerText
                val date = formattedDate(datePicker.headerText)

            }

        }

        binding.btnTo.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()
            datePicker.show(childFragmentManager,"to_tag")

            datePicker.addOnPositiveButtonClickListener {
                binding.btnTo.text = datePicker.headerText
                val date = formattedDate(datePicker.headerText)

            }

        }
    }

    private fun setupAdapter() {
        val adapter = TransactionListAdapter()
        binding.rvTransactionDetail.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactionDetail.adapter = adapter

        viewModel.transactionList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })

    }


}