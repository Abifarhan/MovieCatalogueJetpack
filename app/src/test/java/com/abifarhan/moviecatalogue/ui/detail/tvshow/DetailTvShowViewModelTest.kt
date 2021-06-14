package com.abifarhan.moviecatalogue.ui.detail.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.data.TvRepository
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.utils.DataTvShow
import com.abifarhan.moviecatalogue.vo.Resource
import com.abifarhan.moviecatalogue.vo.Status
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailTvShowViewModelTest {
    private lateinit var viewModel: DetailTvShowViewModel
    private val dataTvShow = DataTvShow.generateTvShowData()[0]
    private val tvShowId = dataTvShow.tvShowId

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var tvRepository: TvRepository

    @Mock
    private lateinit var tvObserver: Observer<Resource<TvShowEntity>>

    @Before
    fun setUp() {
        viewModel = DetailTvShowViewModel(tvRepository)
        viewModel.selectedTv(tvShowId)
    }

    @Test
    fun `setSelectedTv should be success`() {
        val tvResourcesLiveData = MutableLiveData<Resource<TvShowEntity>>().apply {
            value = Resource.success(dataTvShow)
        }
        `when`(tvRepository.getTvWithDetail(tvShowId)).thenReturn(tvResourcesLiveData)
        viewModel.tvWithDetail.observeForever(tvObserver)
        verify(tvObserver).onChanged(viewModel.tvWithDetail.value)
        val tvResources = tvResourcesLiveData.value
        verify(tvRepository).getTvWithDetail(tvShowId)
        assertEquals(Status.SUCCESS, tvResources?.status)
        assertEquals(dataTvShow, tvResources?.data)
        assertEquals(null, tvResources?.message)
        assertEquals(false, tvResources?.data?.favorite)
    }

    @Test
    fun `setFavorite should be success trigger`() {
        val tvResourcesLiveData = MutableLiveData<Resource<TvShowEntity>>().apply {
            value = Resource.success(dataTvShow.also { it.favorite = true })
        }
        `when`(tvRepository.getTvWithDetail(tvShowId)).thenReturn(tvResourcesLiveData)
        viewModel.tvWithDetail.observeForever(tvObserver)
        viewModel.setFavoritTv()
        Mockito.verify(tvObserver).onChanged(tvResourcesLiveData.value)
        val expectedValue = tvResourcesLiveData.value
        Mockito.verify(tvRepository).setTvFavorite(expectedValue?.data as TvShowEntity, false)
        val actualValue = viewModel.tvWithDetail.value
        Assert.assertEquals(expectedValue, actualValue)
    }
}