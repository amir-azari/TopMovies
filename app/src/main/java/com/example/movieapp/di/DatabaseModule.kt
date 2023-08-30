package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.db.MoviesDatabase
import com.example.movieapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context) = Room.databaseBuilder(
        context ,MoviesDatabase::class.java , Constants.MOVIES_DATABASE)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db : MoviesDatabase) = db.movieDao()


    @Provides
    @Singleton
    fun provideEntity() = MovieEntity()


}

