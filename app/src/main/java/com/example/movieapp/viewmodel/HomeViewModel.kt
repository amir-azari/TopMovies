package com.example.movieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.home.ResponseGenresList
import com.example.movieapp.models.home.ResponseMoviesList
import com.example.movieapp.repository.HomeRepository
import com.example.movieapp.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    init {
        loadTopMoviesList(3)
        loadGenresList()
        loadLastMoviesList()
    }

    val topMoviesList = MutableLiveData<MyResponse<ResponseMoviesList>>()
    fun loadTopMoviesList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.topMoviesList(id).collect{
            topMoviesList.postValue(it)
        }
    }

    val genresList = MutableLiveData<MyResponse<ResponseGenresList>>()

    fun loadGenresList() = viewModelScope.launch(Dispatchers.IO) {
        repository.genresList().collect{
            genresList.postValue(it)
        }

    }

    val lastMoviesList = MutableLiveData<MyResponse<ResponseMoviesList>>()
    fun loadLastMoviesList() = viewModelScope.launch(Dispatchers.IO) {
        repository.lastMoviesList().collect{
            lastMoviesList.postValue(it)
        }
    }

    fun loadGenresMoviesList(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.genresMoviesList(id).collect{
            lastMoviesList.postValue(it)
        }
    }

}