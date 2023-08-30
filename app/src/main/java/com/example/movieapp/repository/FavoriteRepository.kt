package com.example.movieapp.repository

import com.example.movieapp.db.MoviesDao
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val dao : MoviesDao) {

    fun allFavoriteList() = dao.getAllMovies()

}