package com.abifarhan.moviecatalogue.ui.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.data.TvRepository
import com.abifarhan.moviecatalogue.vo.Resource

class TvShowViewModel(private val tvRepository: TvRepository) : ViewModel() {
    fun getTvShow(): LiveData<Resource<PagedList<TvShowEntity>>> =
        tvRepository.getAllTv()
}
