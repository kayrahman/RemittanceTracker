package com.example.remittancetracker.ui.transaction.transDetail

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.remittancetracker.R
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.model.FirebaseTransactionInfo
import com.example.remittancetracker.repo.IRepoDataSource
import com.example.remittancetracker.util.TYPE_TRANSACTION_SEND_MONEY
import kotlinx.coroutines.launch
import com.example.remittancetracker.repo.Result
import com.example.remittancetracker.util.A_DAY_IN_LONG
import com.example.remittancetracker.util.TYPE_TOTAL_CASH_IN
import com.example.remittancetracker.util.USER_TYPE_ADMIN
import com.nkr.bazaranocustomer.util.SingleLiveEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionDetailViewModel(val app : Application, val repo : IRepoDataSource) : BaseViewModel(app) {

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

    val startDate = MutableLiveData<Long>(0)
    val endDate = MutableLiveData<Long>(0)

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
            Log.i("transaction_admin","transaction_admin ${transaction_type}")
            repo.getTransactionsInfoFromRemote(transaction_type)
        }else {
            repo.getAgentTransactionsInfoFromRemote(transaction_type)
        }

        when(response){
            is Result.Success -> {
                val transactions = response.data
                transactionList.value = transactions
                showNoData.value = transactions.isEmpty()
                Log.i("transaction_detail","transactions : ${transactions.size.toString()}")
            }
            is Result.Error -> {
                showNoData.value = true
                Log.i("transaction_detail","transactions : error ${response.exception.toString()}")
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

    fun setStartDate(it: Long) {
        startDate.value = it
    }

    fun setEndDate(it: Long) {
        endDate.value = it
    }



    fun filterTransactionsByDate(user_type : String,transaction_type: String) = viewModelScope.launch {
        if (startDate.value != 0L && endDate.value != 0L) {

            Log.i("isSameDay","start_date : ${startDate.value}")
            var end_date:Long = 0
            if(startDate.value == endDate.value){
                end_date = endDate.value!!.plus(A_DAY_IN_LONG)
                Log.i("isSameDay","end_date : ${end_date}")
            }else{
                end_date = endDate.value!!
                Log.i("isSameDay","end_date : ${end_date}")
            }

            val response = if (user_type == USER_TYPE_ADMIN) {
                Log.i("transaction_admin", "transaction_admin ${transaction_type}")
              //  repo.getTransactionsInfoFromRemote(transaction_type)
                repo.getFilteredTransactionsInfoByDateFromRemote(transaction_type,startDate.value!!,end_date)
            } else {
                repo.getFilteredAgentTransactionsInfoByDateFromRemote(transaction_type,startDate.value!!,end_date)
            }

            when (response) {
                is Result.Success -> {
                    val transactions = response.data
                    transactionList.value = transactions
                    showNoData.value = transactions.isEmpty()
                    Log.i("filtered_trans", "transactions : ${transactions.size.toString()}")
                }
                is Result.Error -> {
                    showNoData.value = true
                    Log.i(
                        "filtered_trans",
                        "transactions : error ${response.exception.toString()}"
                    )
                }
            }
        } else {
                showSnackBar.value = "End date is not selected"
        }

    }

}