package com.example.remittancetracker.ui.createAgent

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.model.FirebaseUserInfo
import com.example.remittancetracker.repo.IRepoDataSource
import com.example.remittancetracker.repo.Result
import com.nkr.bazaranocustomer.util.SingleLiveEvent
import kotlinx.coroutines.launch

class CreateAgentViewModel(app: Application, val repo : IRepoDataSource) : BaseViewModel(app) {

    val isAgentInfoUploadSuccessful = SingleLiveEvent<Boolean>()

    fun updateUserInfo(user: FirebaseUserInfo) = viewModelScope.launch {
        showLoading.value = true
        val response = repo.updateAgentInfoIntoRemote(user)
        when (response) {
            is Result.Success -> {
                isAgentInfoUploadSuccessful.value = true
                showLoading.value = false
                showSnackBar.value = "Agent created successfully"
            }
            is Result.Error -> {
                showLoading.value = false
            }
        }

    }


}