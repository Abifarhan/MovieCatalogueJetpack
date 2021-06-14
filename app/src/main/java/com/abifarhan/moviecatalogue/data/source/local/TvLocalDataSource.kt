package com.abifarhan.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.data.source.local.room.TvDao

class TvLocalDataSource private constructor(private val mTvDao: TvDao) {

    companion object {
        private var INSTANCE: TvLocalDataSource? = null

        fun getInstance(tvDao: TvDao): TvLocalDataSource =
            INSTANCE ?: TvLocalDataSource(tvDao)
    }

    fun getAllTv(): DataSource.Factory<Int, TvShowEntity> =
        mTvDao.getTv()

    fun getTvWithDetail(tvId: String): LiveData<TvShowEntity> =
        mTvDao.getTvWithDetail(tvId)

    fun getFavoriteTv(): DataSource.Factory<Int, TvShowEntity> =
        mTvDao.getFavoriteTv()

    fun insertTv(tv: List<TvShowEntity>) =
        mTvDao.insertTv(tv)

    fun insertTvDetail(tv: TvShowEntity) =
        mTvDao.insertTvDetail(tv)

    fun setTvFavorite(tv: TvShowEntity, newState: Boolean) {
        tv.favorite = newState
        mTvDao.updateTv(tv)
    }
}