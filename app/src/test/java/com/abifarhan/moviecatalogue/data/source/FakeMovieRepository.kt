package com.abifarhan.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.MovieDataSource
import com.abifarhan.moviecatalogue.data.NetworkBoundResource
import com.abifarhan.moviecatalogue.data.source.local.LocalDataSource
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.data.source.remote.ApiResponse
import com.abifarhan.moviecatalogue.data.source.remote.RemoteDataSource
import com.abifarhan.moviecatalogue.data.source.remote.response.MovieResponse
import com.abifarhan.moviecatalogue.utils.AppExecutors
import com.abifarhan.moviecatalogue.vo.Resource

class FakeMovieRepository(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource,
    val appExecutors: AppExecutors
) : MovieDataSource {

    override fun getAllMovies(): LiveData<Resource<PagedList<MovieEntity>>> {
        return object :
            NetworkBoundResource<PagedList<MovieEntity>, List<MovieResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllMovies(), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getAllMovies()
            }

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<MovieEntity>()
                for (i in data.indices) {
                    val response = data[i]
                    val movie = MovieEntity(
                        response.id,
                        response.originalTitle,
                        response.posterPath,
                        response.overview,
                        false
                    )
                    movieList.add(movie)
                }
                localDataSource.insertMovies(movieList)
            }
        }.asLiveData()
    }

    override fun getMoviesWithDetail(movieId: String): LiveData<Resource<MovieEntity>> {
        return object : NetworkBoundResource<MovieEntity, MovieResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<MovieEntity> =
                localDataSource.getMovieWithDetail(movieId)

            override fun shouldFetch(data: MovieEntity?): Boolean =
                data?.movieId == null || data.movieId.isEmpty()

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> =
                remoteDataSource.getDetailMovie(movieId)

            override fun saveCallResult(data: MovieResponse) {
                val movie = MovieEntity(
                    data.id,
                    data.originalTitle,
                    data.posterPath,
                    data.overview,
                    false
                )
                localDataSource.insertMovieDetail(movie)
            }
        }.asLiveData()
    }

    override fun setMoviesFavorite(movie: MovieEntity, state: Boolean) =
        appExecutors.diskIO().execute {
            localDataSource.setMovieFavorite(movie, state)
        }

    override fun getMoviesFavorite(): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getFavoriteMovie(), config).build()
    }


}