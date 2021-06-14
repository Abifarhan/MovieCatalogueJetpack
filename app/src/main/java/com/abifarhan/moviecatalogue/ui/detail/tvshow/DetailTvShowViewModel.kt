package com.abifarhan.moviecatalogue.ui.detail.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.data.TvRepository
import com.abifarhan.moviecatalogue.vo.Resource

class DetailTvShowViewModel(private val tvRepository: TvRepository) : ViewModel() {

    val tvId = MutableLiveData<String>()

    fun selectedTv(tvId: String) {
        this.tvId.value = tvId
    }

    val tvWithDetail: LiveData<Resource<TvShowEntity>> =
        Transformations.switchMap(tvId) {
            tvRepository.getTvWithDetail(it)
        }

    fun setFavoritTv() {
        val tvResource = tvWithDetail.value
        if (tvResource != null) {
            val tvWithDetail = tvResource.data
            if (tvWithDetail != null) {
                val newState = !tvWithDetail.favorite
                tvRepository.setTvFavorite(tvWithDetail, newState)
            }
        }
    }
}