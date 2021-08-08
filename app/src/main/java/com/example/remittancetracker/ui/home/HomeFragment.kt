package com.example.remittancetracker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.remittancetracker.R
import com.example.remittancetracker.databinding.HomeFragmentBinding
import com.example.remittancetracker.base.BaseFragment
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.model.FirebaseUserInfo
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_IN
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_OUT
import com.example.remittancetracker.util.TYPE_TRANSACTION_RECEIVE_MONEY
import com.example.remittancetracker.util.TYPE_TRANSACTION_SEND_MONEY
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.nkr.bazaranocustomer.base.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModel()
    override val _viewModel: BaseViewModel
        get() = viewModel

    private lateinit var binding : HomeFragmentBinding
    private val safeArgs : HomeFragmentArgs by navArgs()

    private lateinit var userInfo : FirebaseUserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfo = safeArgs.userInfo
        Log.i("home_user_info",userInfo.toString())
        viewModel.setUserInfo(userInfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getTransactionsTotal(userInfo.user_type)

        setupListener()

    }

    private fun setupListener() {
        binding.cvTotalCashIn.setOnClickListener {
            Log.i("user_type","${userInfo.toString()}")
            navigateToTransactionDetail(TYPE_TOTAL_CASH_IN,userInfo.user_type)
        }
        binding.cvTotalCashOut.setOnClickListener {
            Log.i("user_type","${userInfo.toString()}")
            navigateToTransactionDetail(TYPE_TOTAL_CASH_OUT,userInfo.user_type)
        }




        binding.btnSendMoney.setOnClickListener {
            navigateToTransactionScreen(TYPE_TRANSACTION_SEND_MONEY)
        }

        binding.btnRcvMoney.setOnClickListener {
            navigateToTransactionScreen(TYPE_TRANSACTION_RECEIVE_MONEY)
        }

        binding.btnCreateAgent.setOnClickListener {
            navigateToCreateAgentScreen()
        }


        binding.ibLogout.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
                .addOnCompleteListener {
                    //go to login screen
                    viewModel.navigationCommand.value = NavigationCommand.BackTo(R.id.authenticationFragment)
                }
        }

    }

    private fun navigateToCreateAgentScreen() {
        val actionCreateAgent = HomeFragmentDirections.actionHomeFragmentToCreateAgentFragment()
        viewModel.navigationCommand.value = NavigationCommand.To(actionCreateAgent)
    }


    fun navigateToTransactionScreen(transaction_type : String){
        val actionTransDetail = HomeFragmentDirections.actionHomeFragmentToTransactionFragment(transaction_type,userInfo.user_type)
        viewModel.navigationCommand.value = NavigationCommand.To(actionTransDetail)
    }

    fun navigateToTransactionDetail(transaction_detail_type : String,user_type : String){
        val actionTransDetail = HomeFragmentDirections.actionHomeFragmentToTotalCashInNOutDetailFragment(transaction_detail_type, user_type)
        viewModel.navigationCommand.value = NavigationCommand.To(actionTransDetail)
    }

}