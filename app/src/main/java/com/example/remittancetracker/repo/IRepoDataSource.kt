package com.example.remittancetracker.repo

import android.net.Uri
import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.local.model.MovieDTO

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//
    suspend fun setupUserInRemote(): Result<Unit>
    suspend fun getUserInfoFromRemote() : Result<FirebaseUserInfo>
    suspend fun updateRemoteUserType(type:Int) : Result<Unit>

    suspend fun updateUserSubscriptionPlanToRemote(sub_plan : String) : Result<Unit>



    suspend fun updateAgentInfoIntoRemote(agent_info : FirebaseNewAgent) : Result<Unit>


    //---------------  FirebaseTransactionInfo-------- //
    suspend fun uploadTransactionInfoIntoRemote(info : FirebaseTransactionInfo): Result<Unit>
    suspend fun getTransactionsInfoFromRemote(type : String): Result<List<FirebaseTransactionInfo>>
}