package com.example.remittancetracker.repo

import android.net.Uri
import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.local.model.MovieDTO

interface IRepoDataSource {

    //-------------------REMOTE-------------------------//
    suspend fun setupUserInRemote(): Result<Unit>
    suspend fun getUserInfoFromRemote() : Result<FirebaseUserInfo>
    suspend fun updateRemoteUserType(type:Int) : Result<Unit>
    suspend fun updateUserInfoIntoRemote(user_info : UserInfo) : Result<Unit>
    suspend fun updateUserSubscriptionPlanToRemote(sub_plan : String) : Result<Unit>

    //---------------  FirebaseTransactionInfo-------- //
    suspend fun uploadTransactionInfoIntoRemote(info : FirebaseTransactionInfo): Result<Unit>
}