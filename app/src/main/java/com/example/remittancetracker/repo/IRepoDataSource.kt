package com.example.remittancetracker.repo

import android.net.Uri
import com.example.remittancetracker.model.*

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//
   // suspend fun setupUserInRemote(): Result<Unit>
    suspend fun getUserInfoFromRemote() : Result<FirebaseUserInfo>
    suspend fun updateRemoteUserType(type:Int) : Result<Unit>

    suspend fun updateAdminTokenToRemote(token : String) : Result<Unit>

    suspend fun updateUserSubscriptionPlanToRemote(sub_plan : String) : Result<Unit>
    suspend fun updateAgentInfoIntoRemote(agent_info : FirebaseUserInfo) : Result<Unit>


    //---------------  FirebaseTransactionInfo-------- //
    suspend fun uploadTransactionInfoIntoRemote(info : FirebaseTransactionInfo, user_type:String): Result<Unit>
    suspend fun updateTransactionTotalInto(amount : Int, user_type: String,trans_type : String) : Result<Unit>
    suspend fun getTransactionsInfoFromRemote(type : String): Result<List<FirebaseTransactionInfo>>
    suspend fun getAgentTransactionsInfoFromRemote(type : String): Result<List<FirebaseTransactionInfo>>
    suspend fun getTransactionsTotalFromRemote() : Result<TransactionTotal>
    suspend fun getAgentTransactionsTotalFromRemote() : Result<TransactionTotal>
}