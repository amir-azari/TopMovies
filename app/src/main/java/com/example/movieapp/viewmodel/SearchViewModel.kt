package com.example.movieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.home.ResponseMoviesList
import com.example.movieapp.repository.SearchRepository
import com.example.movieapp.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {
    val moviesList = MutableLiveData<MyResponse<ResponseMoviesList>>()


    fun loadSearch(name: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.searchMovie(name).collect {
            moviesList.postValue(it)
        }


    }


}