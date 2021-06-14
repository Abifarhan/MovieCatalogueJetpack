package com.abifarhan.moviecatalogue.ui.favorit.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.TvRepository
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity

class FavoriteTvViewModel(private val tvRepository: TvRepository) : ViewModel() {

    fun getTvFavorite(): LiveData<PagedList<TvShowEntity>> =
        tvRepository.getTvFavorite()

    fun setTvFavorite(tvShowEntity: TvShowEntity) {
        val newState = !tvShowEntity.favorite
        tvRepository.setTvFavorite(tvShowEntity, newState)
    }
}