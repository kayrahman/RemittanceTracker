package com.example.remittancetracker.ui.transaction

import android.app.Application
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
import timber.log.Timber

class TransactionViewModel(val app : Application, val repo : IRepoDataSource) : BaseViewModel(app) {

    val accountName = MutableLiveData<String>("")
    val accountNumber = MutableLiveData<String>("")
    val amount = MutableLiveData<String>("")
    val city = MutableLiveData<String>("")
    val transactionType = MutableLiveData<String>("")
    val postType = MutableLiveData<String>("")
    val paymentMethodType = MutableLiveData<String>("")


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


    fun postTransactionInfo() = viewModelScope.launch{
      showLoading.value = true
       val ti = validatedTransactionInfo()
        ti?.let {
            val response = repo.uploadTransactionInfoIntoRemote(it)
            when(response){
                is Result.Success -> {
                    Timber.i("post_transaction_staus : true")
                    showLoading.value = false
                }
                is Result.Error -> {
                    Timber.i("post_transaction_staus : error ${response.exception.toString()}")
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
               val amount_num =  Integer.parseInt(acc_number)
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
                return null
            }
        }else{
            showSnackBar.value = "Fields empty"
            return null
        }
    }


}