package com.example.remittancetracker.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.model.FirebaseUserInfo
import com.example.remittancetracker.repo.IRepoDataSource
import kotlinx.coroutines.launch
import com.example.remittancetracker.repo.Result
import com.example.remittancetracker.util.USER_TYPE_ADMIN
import timber.log.Timber


class HomeViewModel(app: Application,val repo : IRepoDataSource) : BaseViewModel(app) {
    val isUserTypeAdmin = MutableLiveData<Boolean>(false)
    val userInfo = MutableLiveData<FirebaseUserInfo>()
    val totalSentMoney = MutableLiveData<Int>()
    val totalReceivedMoney = MutableLiveData<Int>()

    fun setUserInfo(user_info: FirebaseUserInfo){
        userInfo.value = user_info
        if(user_info.user_type == USER_TYPE_ADMIN){
            isUserTypeAdmin.value = true
        }
    }


    fun getTransactionsTotal() = viewModelScope.launch {
        val response = repo.getTransactionsTotalFromRemote()
        when(response){
            is Result.Success -> {
                val trans_detail = response.data
                totalSentMoney.value = trans_detail.total_sent_money
                totalReceivedMoney.value = trans_detail.total_received_money

            }
            is Result.Error -> {
                Timber.i("trans_response : error ${response.exception.toString()}")
            }
        }
    }

}