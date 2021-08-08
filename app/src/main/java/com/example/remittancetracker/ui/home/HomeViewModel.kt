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


class HomeViewModel(app: Application,val repo : IRepoDataSource) : BaseViewModel(app) {
    val isUserTypeAdmin = MutableLiveData<Boolean>(false)
    val userInfo = MutableLiveData<FirebaseUserInfo>()



    fun setUserInfo(user_info: FirebaseUserInfo){
        userInfo.value = user_info
        if(user_info.user_type == USER_TYPE_ADMIN){
            isUserTypeAdmin.value = true
        }
    }

}