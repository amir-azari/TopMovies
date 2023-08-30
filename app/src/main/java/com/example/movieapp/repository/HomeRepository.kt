package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import com.example.movieapp.models.home.ResponseGenresList
import com.example.movieapp.models.home.ResponseMoviesList
import com.example.movieapp.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: ApiServices) {

    suspend fun topMoviesList(id: Int) : Flow<MyResponse<ResponseMoviesList>> {
        return flow {
            when(api.moviesTopList(id).code()){
                in 200..202 -> {
                    emit(MyResponse.success(api.moviesTopList(id).body()))

                }

                422 -> {
                    emit(MyResponse.error(""))

                }

                in 400..499 -> {
                    emit(MyResponse.error(""))


                }

                in 500..599 -> {
                    emit(MyResponse.error(""))


                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun genresList() : Flow<MyResponse<ResponseGenresList>> {
        return flow {
            emit(MyResponse.loading())

            when(api.genresList().code()){
                in 200..202 -> {
                    emit(MyResponse.success(api.genresList().body()))

                }

                422 -> {
                    emit(MyResponse.error(""))

                }

                in 400..499 -> {
                    emit(MyResponse.error(""))


                }

                in 500..599 -> {
                    emit(MyResponse.error(""))


                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun lastMoviesList() : Flow<MyResponse<ResponseMoviesList>> {
        return flow {
            emit(MyResponse.loading())
            when(api.moviesLastList().code()){
                in 200..202 -> {
                    emit(MyResponse.success(api.moviesLastList().body()))

                }

                422 -> {
                    emit(MyResponse.error(""))

                }

                in 400..499 -> {
                    emit(MyResponse.error(""))


                }

                in 500..599 -> {
                    emit(MyResponse.error(""))


                }

            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun genresMoviesList(id : Int) : Flow<MyResponse<ResponseMoviesList>> {

        return flow {
            emit(MyResponse.loading())
            when(api.genresMoviesList(id).code()) {

                in 200..202 -> {
                    emit(MyResponse.success(api.genresMoviesList(id).body()))

                }

                422 -> {
                    emit(MyResponse.error(""))

                }

                in 400..499 -> {
                    emit(MyResponse.error(""))


                }

                in 500..599 -> {
                    emit(MyResponse.error(""))


                }

            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }


}