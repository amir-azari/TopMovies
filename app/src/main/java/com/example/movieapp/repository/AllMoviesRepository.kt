package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import javax.inject.Inject

class AllMoviesRepository @Inject constructor(private val api : ApiServices) {
    suspend fun getAllMovies(page: Int) = api.getAllMovies(page)
}