package com.abifarhan.moviecatalogue.ui.tvshow

import android.content.Context
import com.abifarhan.moviecatalogue.data.TvRepository
import com.abifarhan.moviecatalogue.data.source.local.TvLocalDataSource
import com.abifarhan.moviecatalogue.data.source.local.room.TvDatabase
import com.abifarhan.moviecatalogue.data.source.remote.RemoteDataSource
import com.abifarhan.moviecatalogue.utils.AppExecutors
import com.abifarhan.moviecatalogue.utils.JsonHelper

object TvInjection {

    fun provideRepository(context: Context): TvRepository {
        val database = TvDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = TvLocalDataSource.getInstance(database.tvDao())
        val appExecutors = AppExecutors()

        return TvRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}