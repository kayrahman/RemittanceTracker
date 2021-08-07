package com.example.remittancetracker.ui.authentication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.repo.IRepoDataSource
import com.example.remittencetracker.ui.authentication.FirebaseUserLiveData

class AuthenticationViewModel(app : Application, val repo : IRepoDataSource ) : BaseViewModel(app) {


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