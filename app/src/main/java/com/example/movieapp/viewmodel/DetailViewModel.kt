package com.example.movieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.models.detail.ResponseDetail
import com.example.movieapp.repository.DetailRepository
import com.example.movieapp.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel() {
    //Api
    val detailMovie = MutableLiveData<MyResponse<ResponseDetail>>()
    fun loadDetailMovie(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.detailMovie(id).collect {
            detailMovie.postValue(it)
        }
    }

    //Database
    fun saveMovie(entity: MovieEntity)= viewModelScope.launch(Dispatchers.IO) {
        repository.insertMovie(entity)
    }
    fun deleteMovie(entity: MovieEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMovie(entity)
    }
    val isFavorite = MutableLiveData<Boolean>()
    fun existsMovie(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.existsMovie(id).collect {
            isFavorite.postValue(it)
        }
    }

}