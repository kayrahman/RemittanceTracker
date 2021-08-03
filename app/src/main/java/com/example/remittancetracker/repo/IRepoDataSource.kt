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

    //
    suspend fun uploadVideoInfoToRemote(uri:Uri) : Result<MovieLocationInfo>
    suspend fun uploadMovieThumbImageToRemote(uri:Uri) : Result<String>
    suspend fun uploadMovieInfoIntoRemote(movie:FirebaseMovieInfo) : Result<Unit>

    //movie
    suspend fun fetchNewMoviesFromRemote():Result<List<Movie>>
    suspend fun fetchSlideMoviesFromRemote():Result<List<Movie>>
    suspend fun fetchOwnMoviesFromRemote():Result<List<Movie>>
    suspend fun downloadMovieFromRemote(movie : Movie) : Result<Unit>

    //search
    //-------------------------------search--------------------//
    suspend fun fetchProductsBySearch(queryString: String): Result<List<Movie>>

    //storage
    suspend fun uploadUserThumbImageToRemote(uri:Uri) : Result<String>
    suspend fun updateRemoteImageRef(image_ref : String) : Result<Unit>


    //------------LOCAL-----------------//
    suspend fun insertMoviesIntoLocalDb(movie: MovieDTO) : Result<Unit>
    suspend fun getDownloadedMoviesFromLocalDb() : Result<List<Movie>>

//
}