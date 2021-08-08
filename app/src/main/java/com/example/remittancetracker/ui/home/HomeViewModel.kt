package com.example.remittancetracker.ui.home

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class HomeViewModel(app: Application,val repo : IRepoDataSource) : BaseViewModel(app) {
    val isUserTypeAdmin = MutableLiveData<Boolean>(false)
    val userInfo = MutableLiveData<FirebaseUserInfo>()
    val totalSentMoney = MutableLiveData<Int>(0)
    val totalReceivedMoney = MutableLiveData<Int>(0)

    fun setUserInfo(user_info: FirebaseUserInfo){
        userInfo.value = user_info
        if(user_info.user_type == USER_TYPE_ADMIN){
            isUserTypeAdmin.value = true
        }
    }


    fun getTransactionsTotal(user_type : String) = viewModelScope.launch {
        if(user_type == USER_TYPE_ADMIN) {
            val response = repo.getTransactionsTotalFromRemote()
            when (response) {
                is Result.Success -> {
                    val trans_detail = response.data
                    totalSentMoney.value = trans_detail.total_sent_money
                    totalReceivedMoney.value = trans_detail.total_received_money

                    Timber.i("trans_response : ${response.toString()}")
                    Log.i("trans_response", " ${trans_detail.toString()}")

                }
                is Result.Error -> {
                    Log.i("trans_response", " error ${response.exception.toString()}")
                }
            }
        }else{
            getAgentTransactionTotal()
        }
    }


   suspend fun getAgentTransactionTotal(){
       val response = repo.getAgentTransactionsTotalFromRemote()
       when (response) {
           is Result.Success -> {
               val trans_detail = response.data
               totalSentMoney.value = trans_detail.total_sent_money
               totalReceivedMoney.value = trans_detail.total_received_money

               Timber.i("trans_response : ${response.toString()}")
               Log.i("trans_response", " ${trans_detail.toString()}")

           }
           is Result.Error -> {
               Log.i("trans_response", " error ${response.exception.toString()}")
           }
       }
   }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate() : String {
        val currentDateTime = LocalDateTime.now()
        return  currentDateTime.format(DateTimeFormatter.ISO_DATE)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDay() : String {
        val day = LocalDateTime.now().dayOfWeek.toString()
        return day
    }

}