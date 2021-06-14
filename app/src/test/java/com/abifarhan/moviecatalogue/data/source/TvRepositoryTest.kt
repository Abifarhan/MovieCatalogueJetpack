package com.abifarhan.moviecatalogue.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.abifarhan.moviecatalogue.data.source.local.TvLocalDataSource
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.data.source.remote.RemoteDataSource
import com.abifarhan.moviecatalogue.ui.PagedMovieListUtils
import com.abifarhan.moviecatalogue.utils.AppExecutors
import com.abifarhan.moviecatalogue.utils.DataMovie
import com.abifarhan.moviecatalogue.utils.DataTvShow
import com.abifarhan.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.concurrent.Executor

class TvRepositoryTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(TvLocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)
    private val tvRepository = FakeTvRepository(remote, local, appExecutors)
    private val tvResponse = DataTvShow.generateRemoteTvShowData()
    private val tvId = tvResponse[0].id

    @Test
    fun getAllTv() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getAllTv()).thenReturn(dataSourceFactory)
        tvRepository.getAllTv()

        val tvEntities =
            Resource.success(PagedMovieListUtils.mockPagedList(DataTvShow.generateTvShowData()))
        verify(local).getAllTv()
        assertNotNull(tvEntities.data)
        assertEquals(tvResponse.size.toLong(), tvEntities.data?.size?.toLong())
    }

    @Test
    fun getTvWithDetail() {
        val dummyTvEntity = MutableLiveData<TvShowEntity>().apply {
            value = DataTvShow.generateTvShowData()[0]
        }
        `when`(local.getTvWithDetail(tvId)).thenReturn(dummyTvEntity)

        val tvEntity = LiveDataTestUtil.getValue(
            tvRepository.getTvWithDetail(tvId)
        )
        verify(local).getTvWithDetail(tvId)
        assertNotNull(tvEntity.data)
        assertNotNull(tvEntity.data?.titleTv)
        assertEquals(tvResponse[0].originalTitle, tvEntity.data?.titleTv)
    }

    @Test
    fun getTvFavorite() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getFavoriteTv()).thenReturn(dataSourceFactory)
        tvRepository.getTvFavorite()

        val tvEntities =
            Resource.success(PagedMovieListUtils.mockPagedList(DataTvShow.generateTvShowData()))
        verify(local).getFavoriteTv()
        assertNotNull(tvEntities)
        assertEquals(tvResponse.size.toLong(), tvEntities.data?.size?.toLong())
    }

    @Test
    fun setTvFavorite() {
        val dummyTv = DataTvShow.generateTvShowData()[0]
        `when`(appExecutors.diskIO()).thenReturn(Executor {
            local.setTvFavorite(dummyTv, !dummyTv.favorite)
        })
        tvRepository.setTvFavorite(
            dummyTv,
            !dummyTv.favorite
        )
        Mockito.verify(appExecutors).diskIO()
        Mockito.verify(local).setTvFavorite(dummyTv, !dummyTv.favorite)
    }
}