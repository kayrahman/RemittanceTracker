package com.example.remittancetracker.ui.transaction

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.remittancetracker.base.BaseViewModel
import com.example.remittancetracker.repo.IRepoDataSource

class TransactionViewModel(app : Application, val repo : IRepoDataSource) : BaseViewModel(app) {

}