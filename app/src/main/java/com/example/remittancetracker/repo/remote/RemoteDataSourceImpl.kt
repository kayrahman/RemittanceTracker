package com.example.remittancetracker.repo.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nkr.bazarano.service.MyFirebaseMessagingService
import com.nkr.bazaranocustomer.repo.remote.awaitTaskCompletable
import com.nkr.bazaranocustomer.repo.remote.awaitTaskResult
import com.nkr.bazaranocustomer.repo.remote.awaitTaskResultForVideoUri
import com.nkr.bazaranocustomer.repo.remote.toMovie
import com.nkr.bazaranocustomer.util.StorageUtil
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

    //----------------USER AUTHENTICATION ACTIVITY--------------/
    override suspend fun setupUserInRemote(): Result<Unit> {

        Timber.i("user_first_time:settting up user")
        val user_uid = getActiveUser()
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_USERS).document(user_uid).get()
            )
            if (task.exists()) {
                val user = task.toObject(FirebaseUserInfo::class.java)
                // fetch the user data and insert into local db
                Timber.i("user_first_time:false")
                if (user != null) {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        MyFirebaseMessagingService.addTokenToFirestore(it)
                    }

                    Result.Success(Unit)
                } else {
                    Timber.i("user_first_time:not found")
                    throw Exception("firebase user not found")
                }
            } else {
                Timber.i("user_first_time:true")
                val user = insertUserIntoRemote(user_uid)
                when (user) {
                    is Result.Success -> {
                        //update registrationTokens
                        FirebaseMessaging.getInstance().token.addOnSuccessListener {
                            MyFirebaseMessagingService.addTokenToFirestore(it)
                        }

                        Result.Success(Unit)
                    }
                    is Result.Error -> {
                        throw user.exception
                    }
                }

                //fetch the latest 30 products and categories from remote, save into local db

            }
        } catch (e: Exception) {
            // false
            throw e
        }

    }

    override suspend fun updateUserInfo(user_info: UserInfo): Result<Unit> {
        val map = HashMap<String, Any>()
        if (user_info.user_name.isNotEmpty()) {
            map["user_name"] = user_info.user_name
        }
        if (user_info.phone_number.isNotEmpty()) {
            map["phone_number"] = user_info.phone_number
        }

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



    suspend fun insertUserIntoRemote(user_uid: String): Result<FirebaseUserInfo> {
        val firebaseUser = auth.currentUser
        return try {
            val user = FirebaseUserInfo(
                firebaseUser?.uid.toString(),
                firebaseUser?.displayName.toString(),
                email = firebaseUser?.email.toString(),
                img_url = firebaseUser?.photoUrl.toString()
            )
            awaitTaskCompletable(
                remote.collection(COLLECTION_USERS)
                    .document(user_uid)
                    .set(
                        user
                    )
            )

            Result.Success(user)

        } catch (e: Exception) {
            throw e
        }

    }




    private fun getMoviesFromTask(task: QuerySnapshot?): Result<List<Movie>> {
        val movies = mutableListOf<Movie>()
        task?.documents?.forEach {
            val firebase_movie = it.toObject(FirebaseMovieInfo::class.java)
            Timber.i("firebase_movie ${firebase_movie?.video_url.toString()}")
            val movie = firebase_movie?.toMovie
            movie?.uid = it.id
            movies.add(movie!!)
        }

        return Result.Success(movies)
    }


}
