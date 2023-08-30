package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import com.example.movieapp.models.home.ResponseMoviesList
import com.example.movieapp.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: ApiServices) {
//    suspend fun searchMovie(name: String) = api.searchMovie(name)

    suspend fun searchMovie(name: String) : Flow<MyResponse<ResponseMoviesList>> {
        return flow {
            emit(MyResponse.loading())
            when(api.searchMovie(name).code()){

                in 200..202 -> {
                    emit(MyResponse.success(api.searchMovie(name).body()))

                }

                422 -> {
                    emit(MyResponse.error(api.searchMovie(name).errorBody().toString()))

                }

                in 400..499 -> {
                    emit(MyResponse.error(api.searchMovie(name).errorBody().toString()))


                }

                in 500..599 -> {
                    emit(MyResponse.error(api.searchMovie(name).errorBody().toString()))


                }

            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

}
