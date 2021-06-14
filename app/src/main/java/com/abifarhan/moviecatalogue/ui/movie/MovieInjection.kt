package com.abifarhan.moviecatalogue.ui.movie

import android.content.Context
import com.abifarhan.moviecatalogue.data.MovieRepository
import com.abifarhan.moviecatalogue.data.source.local.LocalDataSource
import com.abifarhan.moviecatalogue.data.source.local.room.MovieDatabase
import com.abifarhan.moviecatalogue.data.source.remote.RemoteDataSource
import com.abifarhan.moviecatalogue.utils.AppExecutors
import com.abifarhan.moviecatalogue.utils.JsonHelper


object MovieInjection {

    fun provideRepository(context: Context): MovieRepository {
        val database = MovieDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(database.movieDao())
        val appExecutors = AppExecutors()

        return MovieRepository.getInstance(remoteDataSource,localDataSource,appExecutors)
    }
}