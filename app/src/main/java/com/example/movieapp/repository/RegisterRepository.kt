package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import com.example.movieapp.models.register.BodyRegister
import com.example.movieapp.models.register.ResponseRegister
import com.example.movieapp.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val api: ApiServices) {
    suspend fun registerUser(body: BodyRegister): Flow<MyResponse<ResponseRegister>> {
        return flow {
            emit(MyResponse.loading())
            when (api.registerUser(body).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.registerUser(body).body()))

                }

                422 -> {
                    emit(MyResponse.error("Already registered with this email"))

                }

                in 400..499 -> {
                    emit(MyResponse.error(api.registerUser(body).errorBody().toString()))


                }

                in 500..599 -> {
                    emit(MyResponse.error(api.registerUser(body).errorBody().toString()))


                }

            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }


}