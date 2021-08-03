package com.example.remittancetracker.repo.local

import com.nkr.bazaranocustomer.repo.remote.toMovies
import com.example.remittancetracker.model.Movie
import com.example.remittancetracker.repo.Result
import com.example.remittancetracker.repo.local.model.MovieDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSourceImpl(val dao: MovieDao) : ILocalDataSource {

    override suspend fun insertMovie(movie: MovieDTO): Result<Unit> {
        return withContext(Dispatchers.Default) {
            try {
                dao.insertMovie(movie)
                Result.Success(Unit)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getDownloadedMovie(): Result<List<Movie>> {
        return withContext(Dispatchers.Default) {
            try {
               val movies = dao.getAllDownloadedMovies()
                Result.Success(movies.toMovies)
            } catch (e: Exception) {
                throw e
            }
        }
    }


}