package com.abifarhan.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.data.source.local.room.MovieDao

class LocalDataSource private constructor(
    private val mMovieDao: MovieDao
) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(movieDao: MovieDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(movieDao)
    }

    fun getAllMovies(): DataSource.Factory<Int, MovieEntity> =
        mMovieDao.getMovie()

    fun getMovieWithDetail(movieId: String): LiveData<MovieEntity> =
        mMovieDao.getMovieWithDetail(movieId)

    fun getFavoriteMovie(): DataSource.Factory<Int, MovieEntity> =
        mMovieDao.getFavoriteMovie()


    fun insertMovies(movies: List<MovieEntity>) =
        mMovieDao.insertMovies(movies)

    fun insertMovieDetail(movie: MovieEntity) =
        mMovieDao.insertMoviesDetail(movie)


    fun setMovieFavorite(movie: MovieEntity, newState: Boolean) {
        movie.favorite = newState
        mMovieDao.updateMovie(movie)
    }


}