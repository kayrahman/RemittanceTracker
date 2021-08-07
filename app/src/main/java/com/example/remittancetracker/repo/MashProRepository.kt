package com.example.remittancetracker.repo

import android.net.Uri
import com.nkr.bazaranocustomer.repo.remote.toMovieDTO
import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.local.ILocalDataSource
import com.example.remittancetracker.repo.local.model.MovieDTO
import com.example.remittancetracker.repo.remote.IRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MashProRepository(
    val remote: IRemoteDataSource,
    val local: ILocalDataSource,
) : IRepoDataSource {

    override suspend fun setupUserInRemote(): Result<Unit> {
        return remote.setupUserInRemote()
    }

    override suspend fun getUserInfoFromRemote(): Result<FirebaseUserInfo> {
        return remote.getUserInfo()
    }

    override suspend fun updateRemoteUserType(type: Int): Result<Unit> {
        return remote.updateUserType(type)
    }

    override suspend fun updateUserInfoIntoRemote(user_info: UserInfo): Result<Unit> {
        return remote.updateUserInfo(user_info)
    }

    override suspend fun updateUserSubscriptionPlanToRemote(sub_plan: String): Result<Unit> {
        return remote.updateUserSubscriptionPlan(sub_plan)
    }

    override suspend fun uploadTransactionInfoIntoRemote(info: FirebaseTransactionInfo): Result<Unit> {
        return remote.uploadTransactionInfo(info)
    }


}

