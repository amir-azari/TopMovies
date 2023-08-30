package com.example.movieapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(entity: MovieEntity)

    @Delete
    suspend fun deleteMovie(entity: MovieEntity)

    @Query("SELECT * FROM ${Constants.MOVIES_TABLE}")
    fun getAllMovies() : Flow<MutableList<MovieEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM ${Constants.MOVIES_TABLE} WHERE id = :movieId)")
    fun existsMovie(movieId:Int): Flow<Boolean>

}