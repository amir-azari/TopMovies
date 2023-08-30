package com.example.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.movieapp.paging.MoviesPagingSource
import com.example.movieapp.repository.AllMoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(private val repository: AllMoviesRepository) : ViewModel(){
    val moviesList = Pager(PagingConfig(1 )){
        MoviesPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}