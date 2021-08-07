package com.example.remittancetracker.ui.authentication

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.model.FirebaseNewAgent
import com.example.remittancetracker.repo.IRepoDataSource
import com.example.remittencetracker.ui.authentication.FirebaseUserLiveData
import kotlinx.coroutines.launch
import com.example.remittancetracker.repo.Result
import com.nkr.bazaranocustomer.util.SingleLiveEvent

class AuthenticationViewModel(app : Application, val repo : IRepoDataSource ) : BaseViewModel(app) {

    val isAgentInfoUploadSuccessful = SingleLiveEvent<Boolean>()

    fun updateUserInfo(user: FirebaseNewAgent) = viewModelScope.launch {
            showLoading.value = true
            val response = repo.updateAgentInfoIntoRemote(user)
            when(response){
                is Result.Success ->{
                    isAgentInfoUploadSuccessful.value = true
                    showLoading.value = false
                    showSnackBar.value = "Agent created successfully"
                }
                is Result.Error ->{
                    showLoading.value = false
                }
            }

    }


    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }


}