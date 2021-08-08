package com.example.remittancetracker.repo.remote

import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.Result


interface IRemoteDataSource {

    //------------------NOTIFICAITON------------------//
  //  suspend fun getRegistrationToken() : Result<ArrayList<String>>
   // suspend fun setFCMRegistrationToken(tokens:ArrayList<String>) : Result<Unit>


    //------------------ USER ------------------------------//
   // suspend fun setupUserInRemote() : Result<Unit>

    suspend fun updateUserSubscriptionPlan(sub_plan : String) : Result<Unit>
    suspend fun getUserInfo() : Result<FirebaseUserInfo>
    suspend fun updateUserType(user_type:Int) : Result<Unit>


    suspend fun updateAgentInfo(user_info : FirebaseUserInfo) : Result<Unit>

    //----------------- TransactionInfo ------------------//
    suspend fun uploadTransactionInfo(info: FirebaseTransactionInfo, user_type: String) : Result<Unit>
    suspend fun updateTransactionTotal(amount : Int, user_type: String,trans_type : String) : Result<Unit>
    suspend fun getTransactions(type : String) : Result<List<FirebaseTransactionInfo>>
    suspend fun getAgentTransactions(type : String) : Result<List<FirebaseTransactionInfo>>
    suspend fun getTransactionsTotal() : Result<TransactionTotal>


}









