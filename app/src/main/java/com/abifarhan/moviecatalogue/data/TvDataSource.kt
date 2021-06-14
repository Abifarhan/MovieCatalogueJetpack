package com.abifarhan.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.vo.Resource

interface TvDataSource {

    fun getAllTv(): LiveData<Resource<PagedList<TvShowEntity>>>

    fun getTvWithDetail(tvId: String): LiveData<Resource<TvShowEntity>>

    fun setTvFavorite(tv: TvShowEntity, state: Boolean)

    fun getTvFavorite(): LiveData<PagedList<TvShowEntity>>
}