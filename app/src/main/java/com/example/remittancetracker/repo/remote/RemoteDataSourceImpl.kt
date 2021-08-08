package com.example.remittancetracker.repo.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import com.nkr.bazarano.service.MyFirebaseMessagingService
import com.nkr.bazaranocustomer.repo.remote.awaitTaskCompletable
import com.nkr.bazaranocustomer.repo.remote.awaitTaskResult
import com.example.remittancetracker.model.*
import timber.log.Timber
import com.example.remittancetracker.repo.Result
import com.example.remittancetracker.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RemoteDataSourceImpl(
    val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IRemoteDataSource {

    private fun getActiveUser(): String {
        return auth.currentUser?.uid.toString()
    }

    /*

    override suspend fun getRegistrationToken(): Result<ArrayList<String>> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_USERS)
                    .document(getActiveUser())
                    .get()
            )

            if (task.exists()) {
                val user = task.toObject(FirebaseUserInfo::class.java)
                Result.Success(user?.registration_tokens!!)

            } else {
                throw Exception("user not found")
            }
        } catch (e: Exception) {
            // false
            Result.Error(e)
        }

    }

    override suspend fun setFCMRegistrationToken(tokens: ArrayList<String>): Result<Unit> {
        val user_map = HashMap<String, Any>()
        user_map["registration_tokens"] = tokens

        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(getActiveUser())
                    .update(user_map)
            )

            Result.Success(Unit)

        } catch (e: Exception) {
            // false
            Result.Error(e)
        }
    }

    */


    //----------------USER AUTHENTICATION ACTIVITY--------------/

    override suspend fun updateAgentInfo(agent_info: FirebaseUserInfo): Result<Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(agent_info.uid)
                    .set(agent_info)
            )
            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }


    }

    override suspend fun updateUserSubscriptionPlan(sub_plan: String): Result<Unit> {
        val map = HashMap<String, Any>()
        map["subscription_plan"] = sub_plan

        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(getActiveUser())
                    .update(map)
            )
            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun getUserInfo(): Result<FirebaseUserInfo> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_USERS).document(getActiveUser()).get()
            )

            if (task.exists()) {
                val user = task.toObject(FirebaseUserInfo::class.java)
                Result.Success(user!!)
            } else {
                throw Exception("no user found")
            }

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUserType(user_type: Int): Result<Unit> {
        val type = HashMap<String, Any>()
        type["user_type"] = user_type

        return withContext(Dispatchers.IO) {
            try {
                val user_uid = getActiveUser()
                awaitTaskCompletable(
                    remote.collection(COLLECTION_USERS).document(user_uid)
                        .update(type)
                )

                Result.Success(Unit)
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }
    }

    override suspend fun uploadTransactionInfo(info: FirebaseTransactionInfo): Result<Unit> {
        val trns_info = info.copy(uid = getActiveUser()+info.timestamp,creator = getActiveUser())
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_TRANSACTIONS)
                    .document(trns_info.uid)
                    .set(trns_info)
            )
            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTransactions(type : String): Result<List<FirebaseTransactionInfo>> {
        if (type == TYPE_TOTAL_CASH_OUT) {
            return try {
                val task = awaitTaskResult(
                    remote.collection(COLLECTION_TRANSACTIONS)
                        .whereEqualTo("transaction_type", TYPE_TRANSACTION_SEND_MONEY)
                        .get()
                )

                getTransactionsFromTask(task)

            } catch (e: Exception) {
                Result.Error(e)
            }
        }else{
            return try {
                val task = awaitTaskResult(
                    remote.collection(COLLECTION_TRANSACTIONS)
                        .whereEqualTo("transaction_type", TYPE_TRANSACTION_RECEIVE_MONEY)
                        .get()
                )

                getTransactionsFromTask(task)

            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getAgentTransactions(type: String): Result<List<FirebaseTransactionInfo>> {
        if (type == TYPE_TOTAL_CASH_OUT) {
            return try {
                val task = awaitTaskResult(
                    remote.collection(COLLECTION_TRANSACTIONS)
                        .whereEqualTo("creator",getActiveUser())
                        .whereEqualTo("transaction_type", TYPE_TRANSACTION_SEND_MONEY)
                        .get()
                )

                getTransactionsFromTask(task)

            } catch (e: Exception) {
                Result.Error(e)
            }
        }else{
            return try {
                val task = awaitTaskResult(
                    remote.collection(COLLECTION_TRANSACTIONS)
                        .whereEqualTo("creator",getActiveUser())
                        .whereEqualTo("transaction_type", TYPE_TRANSACTION_RECEIVE_MONEY)
                        .get()
                )

                getTransactionsFromTask(task)

            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    private fun getTransactionsFromTask(task: QuerySnapshot?): Result<List<FirebaseTransactionInfo>> {
        val movies = mutableListOf<FirebaseTransactionInfo>()
            task?.documents?.forEach {
                val transactionInfo = it.toObject(FirebaseTransactionInfo::class.java)
                movies.add(transactionInfo!!)
            }

            return Result.Success(movies)

    }

}
