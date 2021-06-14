package com.abifarhan.moviecatalogue.ui.favorit.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.MovieRepository
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity

class FavoriteMovieViewModel(
    private val movieRepository:
    MovieRepository
) : ViewModel() {

    fun getFavorite(): LiveData<PagedList<MovieEntity>> =
        movieRepository.getMoviesFavorite()

    fun setFavorite(movieEntity: MovieEntity) {
        val newState = !movieEntity.favorite
        movieRepository.setMoviesFavorite(movieEntity, newState)
    }
}