package com.abifarhan.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.abifarhan.moviecatalogue.data.NetworkBoundResource
import com.abifarhan.moviecatalogue.data.TvDataSource
import com.abifarhan.moviecatalogue.data.source.local.TvLocalDataSource
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.data.source.remote.ApiResponse
import com.abifarhan.moviecatalogue.data.source.remote.RemoteDataSource
import com.abifarhan.moviecatalogue.data.source.remote.response.TvResponse
import com.abifarhan.moviecatalogue.utils.AppExecutors
import com.abifarhan.moviecatalogue.vo.Resource

class FakeTvRepository(
    val remoteDataSource: RemoteDataSource,
    val tvLocalDataSource: TvLocalDataSource,
    val appExecutors: AppExecutors
) : TvDataSource {

    override fun getAllTv(): LiveData<Resource<PagedList<TvShowEntity>>> {

        return object : NetworkBoundResource<PagedList<TvShowEntity>,
                List<TvResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TvShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    tvLocalDataSource.getAllTv(),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<TvShowEntity>?): Boolean =
                data == null || data.isEmpty()


            override fun createCall(): LiveData<ApiResponse<List<TvResponse>>> =
                remoteDataSource.getAllTv()

            override fun saveCallResult(data: List<TvResponse>) {
                val tvList = ArrayList<TvShowEntity>()
                for (response in data) {
                    val tv = TvShowEntity(
                        response.id,
                        response.originalTitle,
                        response.posterPath,
                        response.overview,
                        false
                    )
                    tvList.add(tv)
                }
                tvLocalDataSource.insertTv(tvList)
            }

        }.asLiveData()

    }

    override fun getTvWithDetail(tvId: String): LiveData<Resource<TvShowEntity>> {
        return object : NetworkBoundResource<TvShowEntity, TvResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<TvShowEntity> =
                tvLocalDataSource.getTvWithDetail(tvId)


            override fun shouldFetch(data: TvShowEntity?): Boolean =
                data?.tvShowId == null || data.tvShowId.isEmpty()

            override fun createCall(): LiveData<ApiResponse<TvResponse>> =
                remoteDataSource.getDetailTv(tvId)

            override fun saveCallResult(data: TvResponse) {
                val tv = TvShowEntity(
                    data.id,
                    data.originalTitle,
                    data.posterPath,
                    data.overview,
                    false
                )
                tvLocalDataSource.insertTvDetail(tv)
            }

        }.asLiveData()
    }

    override fun getTvFavorite(): LiveData<PagedList<TvShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(tvLocalDataSource.getFavoriteTv(), config).build()
    }

    override fun setTvFavorite(tv: TvShowEntity, state: Boolean) {
        appExecutors.diskIO().execute {
            tvLocalDataSource.setTvFavorite(tv, state)
        }
    }
}