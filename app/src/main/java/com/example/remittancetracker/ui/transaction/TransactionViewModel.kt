package com.example.remittancetracker.ui.transaction

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.remittancetracker.R
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.model.FirebaseTransactionInfo
import com.example.remittancetracker.repo.IRepoDataSource
import com.example.remittancetracker.util.TYPE_TRANSACTION_SEND_MONEY
import kotlinx.coroutines.launch
import com.example.remittancetracker.repo.Result
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_IN
import com.example.remittancetracker.util.USER_TYPE_ADMIN
import com.nkr.bazaranocustomer.util.SingleLiveEvent
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel(val app : Application, val repo : IRepoDataSource) : BaseViewModel(app) {

    val accountName = MutableLiveData<String>("")
    val accountNumber = MutableLiveData<String>("")
    val amount = MutableLiveData<String>("")
    val city = MutableLiveData<String>("")
    val transactionType = MutableLiveData<String>("")
    val transactionDetailType = MutableLiveData<String>("")
    val postType = MutableLiveData<String>("")
    val paymentMethodType = MutableLiveData<String>("")

    val transactionList = MutableLiveData<List<FirebaseTransactionInfo>>()

    val isUploadSuccessful = SingleLiveEvent<Boolean>()

    fun setPaymentMethodType(payment_type: String) {
        paymentMethodType.value = payment_type
    }

    fun setTransactionType(transaction_type: String) {
        if(transaction_type == TYPE_TRANSACTION_SEND_MONEY){
            postType.value = app.applicationContext.resources.getString(R.string.label_send)
        }else{
            postType.value = app.applicationContext.resources.getString(R.string.label_receive)
        }
        transactionType.value = transaction_type
    }


    fun getTransactions(user_type : String,transaction_type: String) = viewModelScope.launch {
        val response = if(user_type == USER_TYPE_ADMIN) {
            repo.getTransactionsInfoFromRemote(transaction_type)
        }else{
            repo.getAgentTransactionsInfoFromRemote(transaction_type)
        }


        when(response){
            is Result.Success -> {
                val transactions = response.data
                if(transactions.isNotEmpty()){
                    transactionList.value = transactions
                }else{
                    showNoData.value = true
                }
                Log.i("transaction_detail","transactions : ${transactions.size.toString()}")
            }
            is Result.Error -> {
                showNoData.value = true
                Log.i("transaction_detail","transactions : error ${response.exception.toString()}")
            }
        }
    }


    fun postTransactionInfo(user_type: String) = viewModelScope.launch{

       val ti = validatedTransactionInfo()
        ti?.let {
            showLoading.value = true
            val response = repo.uploadTransactionInfoIntoRemote(it,user_type)
            when(response){
                is Result.Success -> {
                    Timber.i("post_transaction_staus : true")
                    Log.i("post","status : true")

                    val total_response = repo.updateTransactionTotalInto(it.amount,user_type,it.transaction_type)
                    when(total_response){
                        is Result.Success -> {
                            isUploadSuccessful.value = true
                            showLoading.value = false
                        }
                        is Result.Error -> {
                            showLoading.value = false
                            Timber.i("post_transaction_staus : error ${total_response.exception.toString()}")
                        }
                    }


                }
                is Result.Error -> {
                    Timber.i("post_transaction_staus : error ${response.exception.toString()}")
                    Log.i("post","status : false")
                    showLoading.value = false
                }
            }
        }
    }

    private fun validatedTransactionInfo() : FirebaseTransactionInfo? {
        val acc_name = accountName.value.toString().trim()
        val acc_number = accountNumber.value.toString().trim()
        val amount = amount.value.toString().trim()
        val city = city.value.toString().trim()
        val payment_type = paymentMethodType.value.toString().trim()
        val transaction_type = transactionType.value.toString().trim()

        if(acc_name.isNotEmpty() && amount.isNotEmpty() && acc_number.isNotEmpty()
            && payment_type.isNotEmpty() && transaction_type.isNotEmpty()){
            try{
               val amount_num =  Integer.parseInt(amount)
                return if(amount_num!= 0){
                    FirebaseTransactionInfo(
                        account_name = acc_name,
                        amount = amount_num,
                        account_number = acc_number,
                        city = city,
                        payment_type = payment_type,
                        transaction_type = transaction_type
                    )
                }else{
                    showSnackBar.value = "Fields Empty"
                    null
                }

            }catch (e:Exception){
                showSnackBar.value = "Something went wrong"
                return null
            }
        }else{
            showSnackBar.value = "Fields empty"
            return null
        }
    }


    fun setTransactionDetailType(type : String){
        if(type == TYPE_TOTAL_CASH_IN) {
            transactionDetailType.value = "Total Cash In"
        }else{
            transactionDetailType.value = "Total Cash Out"
        }
    }


}