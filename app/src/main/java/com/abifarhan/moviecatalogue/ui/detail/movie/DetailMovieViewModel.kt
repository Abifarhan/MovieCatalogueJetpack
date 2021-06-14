package com.abifarhan.moviecatalogue.ui.detail.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.data.MovieRepository
import com.abifarhan.moviecatalogue.vo.Resource

class DetailMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    val movieId = MutableLiveData<String>()

    fun selectedMovie(movieId: String) {
        this.movieId.value = movieId
    }

    val movieWithDetail: LiveData<Resource<MovieEntity>> =
        Transformations.switchMap(movieId) {
            movieRepository.getMoviesWithDetail(it)
        }

    fun setFavoriteMovie() {
        val movieResource = movieWithDetail.value
        if (movieResource != null) {
            val movieWithDetail = movieResource.data
            if (movieWithDetail != null) {
                val newState = !movieWithDetail.favorite
                movieRepository.setMoviesFavorite(movieWithDetail, newState)
            }
        }
    }
}
