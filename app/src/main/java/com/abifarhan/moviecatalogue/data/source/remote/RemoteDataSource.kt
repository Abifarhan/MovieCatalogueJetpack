package com.abifarhan.moviecatalogue.data.source.remote

import android.os.Looper
import com.abifarhan.moviecatalogue.utils.JsonHelper
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abifarhan.moviecatalogue.data.source.remote.response.MovieResponse
import com.abifarhan.moviecatalogue.data.source.remote.response.TvResponse
import com.abifarhan.moviecatalogue.utils.EspressoIdlingResources

class RemoteDataSource private constructor(private val jsonHelper: JsonHelper) {
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val SERVICE_LATENCY_IN_MILLIS: Long = 2000

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: JsonHelper): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(helper).apply {
                    instance = this
                }
            }
    }

    fun getAllMovies():
            LiveData<ApiResponse<List<MovieResponse>>> {
        EspressoIdlingResources.increment()
        val resultMovie = MutableLiveData<ApiResponse<List<MovieResponse>>>()
        handler.postDelayed({
            resultMovie.value =
                ApiResponse.success(
                    jsonHelper
                        .loadMovie()
                )
            EspressoIdlingResources.decrement()
        }, SERVICE_LATENCY_IN_MILLIS)
        return resultMovie
    }

    fun getDetailMovie(movieId: String):
            LiveData<ApiResponse<MovieResponse>> {
        EspressoIdlingResources.increment()
        val resultDetail = MutableLiveData<ApiResponse<MovieResponse>>()
        handler.postDelayed(
            {
                resultDetail.value = ApiResponse.success(jsonHelper.loadMovieDetail(movieId))
                EspressoIdlingResources.decrement()
            },
            SERVICE_LATENCY_IN_MILLIS
        )
        return resultDetail
    }


    fun getAllTv(): LiveData<ApiResponse<List<TvResponse>>> {
        EspressoIdlingResources.increment()
        val resultTv = MutableLiveData<ApiResponse<List<TvResponse>>>()
        handler.postDelayed({
            resultTv.value =
                ApiResponse.success(
                    jsonHelper.loadTv()
                )
            EspressoIdlingResources.decrement()
        }, SERVICE_LATENCY_IN_MILLIS)

        return resultTv
    }

    fun getDetailTv(tvId: String):
            LiveData<ApiResponse<TvResponse>> {
        EspressoIdlingResources.increment()
        val resultDetail = MutableLiveData<ApiResponse<TvResponse>>()
        handler.postDelayed(
            {
                resultDetail.value = ApiResponse.success(jsonHelper.loadTvDetail(tvId))
                EspressoIdlingResources.decrement()
            },
            SERVICE_LATENCY_IN_MILLIS
        )
        return resultDetail
    }

}