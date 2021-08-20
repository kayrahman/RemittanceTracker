package com.example.remittancetracker.repo

import com.example.remittancetracker.model.*
import com.example.remittancetracker.repo.remote.IRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MashProRepository(
    val remote: IRemoteDataSource
) : IRepoDataSource {


    override suspend fun getUserInfoFromRemote(): Result<FirebaseUserInfo> {
        return remote.getUserInfo()
    }

    override suspend fun updateRemoteUserType(type: Int): Result<Unit> {
        return remote.updateUserType(type)
    }

    override suspend fun updateAdminTokenToRemote(token: String): Result<Unit> {
        return remote.updateAdminToken(token)
    }

    override suspend fun updateAgentInfoIntoRemote(agent_info: FirebaseUserInfo): Result<Unit> {
        return remote.updateAgentInfo(agent_info)
    }

    override suspend fun updateUserSubscriptionPlanToRemote(sub_plan: String): Result<Unit> {
        return remote.updateUserSubscriptionPlan(sub_plan)
    }

    override suspend fun uploadTransactionInfoIntoRemote(info: FirebaseTransactionInfo,user_type:String): Result<Unit> {
        return remote.uploadTransactionInfo(info,user_type)
    }

    override suspend fun updateTransactionTotalInto(
        amount: Int,
        user_type: String,
        trans_type: String
    ): Result<Unit> {
        return remote.updateTransactionTotal(amount,user_type,trans_type)
    }

    override suspend fun getTransactionsInfoFromRemote(trns_type : String): Result<List<FirebaseTransactionInfo>> {
        return remote.getTransactions(trns_type)
    }

    override suspend fun getFilteredTransactionsInfoByDateFromRemote(
        type: String,
        start_date: Long,
        end_date: Long
    ): Result<List<FirebaseTransactionInfo>> {
        return withContext(Dispatchers.IO){
            remote.getFilteredTransactionsByDate(type,start_date,end_date)
        }
    }

    override suspend fun getAgentTransactionsInfoFromRemote(type: String): Result<List<FirebaseTransactionInfo>> {
        return remote.getAgentTransactions(type)
    }

    override suspend fun getFilteredAgentTransactionsInfoByDateFromRemote(
        type: String,
        start_date: Long,
        end_date: Long
    ): Result<List<FirebaseTransactionInfo>> {
        return withContext(Dispatchers.IO){
            remote.getFilteredAgentTransactionsByDate(type,start_date,end_date)
        }
    }

    override suspend fun getTransactionsTotalFromRemote(): Result<TransactionTotal> {
        return remote.getTransactionsTotal()
    }

    override suspend fun getAgentTransactionsTotalFromRemote(): Result<TransactionTotal> {
        return remote.getAgentTransactionsTotal()
    }


}

