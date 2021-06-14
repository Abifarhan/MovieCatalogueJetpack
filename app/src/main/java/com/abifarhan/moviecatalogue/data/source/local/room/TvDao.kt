package com.abifarhan.moviecatalogue.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity

@Dao
interface TvDao {

    @Query("SELECT * FROM tventities")
    fun getTv(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM tventities WHERE favorite = 1")
    fun getFavoriteTv(): DataSource.Factory<Int, TvShowEntity>

    @Transaction
    @Query("SELECT * FROM tventities WHERE tvId = :tvId")
    fun getTvWithDetail(tvId: String): LiveData<TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTv(tv: List<TvShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvDetail(tv: TvShowEntity)

    @Update
    fun updateTv(tv: TvShowEntity)
}