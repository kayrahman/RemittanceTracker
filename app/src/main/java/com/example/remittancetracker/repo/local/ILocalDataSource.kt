package com.example.remittancetracker.repo.local


import com.example.remittancetracker.model.Movie
import com.example.remittancetracker.repo.local.model.MovieDTO
import com.example.remittancetracker.repo.Result


interface ILocalDataSource {
    //--------------------MOVIE-----------------------//
    suspend fun insertMovie(movie : MovieDTO): Result<Unit>
    suspend fun getDownloadedMovie(): Result<List<Movie>>


}