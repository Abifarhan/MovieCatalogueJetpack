package com.abifarhan.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.vo.Resource

interface MovieDataSource {

    fun getAllMovies(): LiveData<Resource<PagedList<MovieEntity>>>

    fun getMoviesWithDetail(movieId: String):
            LiveData<Resource<MovieEntity>>

    fun setMoviesFavorite(movie: MovieEntity, state: Boolean)

    fun getMoviesFavorite(): LiveData<PagedList<MovieEntity>>
}