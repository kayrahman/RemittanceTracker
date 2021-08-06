package com.example.remittancetracker.repo.remote

import android.net.Uri
import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.Result


interface IRemoteDataSource {

    //------------------NOTIFICAITON------------------//
    suspend fun getRegistrationToken() : Result<ArrayList<String>>
    suspend fun setFCMRegistrationToken(tokens:ArrayList<String>) : Result<Unit>


    //------------------ USER ------------------------------//
    suspend fun setupUserInRemote() : Result<Unit>
    suspend fun updateUserInfo(user_info : UserInfo) : Result<Unit>
    suspend fun updateUserSubscriptionPlan(sub_plan : String) : Result<Unit>
    suspend fun getUserInfo() : Result<FirebaseUserInfo>
    suspend fun updateUserType(user_type:Int) : Result<Unit>


}









