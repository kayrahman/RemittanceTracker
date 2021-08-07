package com.example.remittancetracker.repo.remote

import android.net.Uri
import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.Result
import com.google.firebase.ktx.Firebase


interface IRemoteDataSource {

    //------------------NOTIFICAITON------------------//
    suspend fun getRegistrationToken() : Result<ArrayList<String>>
    suspend fun setFCMRegistrationToken(tokens:ArrayList<String>) : Result<Unit>


    //------------------ USER ------------------------------//
    suspend fun setupUserInRemote() : Result<Unit>

    suspend fun updateUserSubscriptionPlan(sub_plan : String) : Result<Unit>
    suspend fun getUserInfo() : Result<FirebaseUserInfo>
    suspend fun updateUserType(user_type:Int) : Result<Unit>


    suspend fun updateAgentInfo(user_info : FirebaseNewAgent) : Result<Unit>

    //----------------- TransactionInfo ------------------//
    suspend fun uploadTransactionInfo(info : FirebaseTransactionInfo) : Result<Unit>
    suspend fun getTransactions(type : String) : Result<List<FirebaseTransactionInfo>>


}









