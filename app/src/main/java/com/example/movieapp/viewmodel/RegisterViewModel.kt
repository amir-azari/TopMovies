package com.example.movieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.register.BodyRegister
import com.example.movieapp.models.register.ResponseRegister
import com.example.movieapp.repository.RegisterRepository
import com.example.movieapp.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository) :
    ViewModel() {

    val registerUser = MutableLiveData<MyResponse<ResponseRegister>>()


    fun registerUsr(body: BodyRegister) = viewModelScope.launch(Dispatchers.IO) {
        repository.registerUser(body).collect{
            registerUser.postValue(it)
        }

    }

}