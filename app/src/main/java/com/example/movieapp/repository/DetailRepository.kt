package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.db.MoviesDao
import com.example.movieapp.models.detail.ResponseDetail
import com.example.movieapp.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailRepository @Inject constructor(private val api: ApiServices,private val dao : MoviesDao) {
    suspend fun insertMovie(entity: MovieEntity) = dao.insertMovie(entity)
    suspend fun deleteMovie(entity: MovieEntity) = dao.deleteMovie(entity)
    fun existsMovie(id:Int)  = dao.existsMovie(id)
    //Api
    suspend fun detailMovie(id: Int) : Flow<MyResponse<ResponseDetail>> {
        return flow {
            emit(MyResponse.loading())

            when(api.detailMovie(id).code()){
                in 200..202 -> {
                    emit(MyResponse.success(api.detailMovie(id).body()))

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