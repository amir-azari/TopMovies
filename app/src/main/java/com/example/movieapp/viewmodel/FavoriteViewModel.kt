package com.example.movieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.repository.FavoriteRepository
import com.example.movieapp.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository) :
    ViewModel() {

    val favoriteList = MutableLiveData<DataStatus<List<MovieEntity>>>()

    fun loadFavoriteList() = viewModelScope.launch(Dispatchers.IO) {
        repository.allFavoriteList().collect {
            favoriteList.postValue(DataStatus.success(it, it.isEmpty()))
        }

    }

}