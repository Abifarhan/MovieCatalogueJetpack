package com.abifarhan.moviecatalogue.ui.detail.movie

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.abifarhan.moviecatalogue.data.MovieRepository
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.utils.DataMovie
import com.abifarhan.moviecatalogue.vo.Resource
import com.abifarhan.moviecatalogue.vo.Status
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailMovieViewModelTest {

    private lateinit var viewModel: DetailMovieViewModel
    private val dataMovie = DataMovie.generateMovieData()[0]
    private val movieId = dataMovie.movieId

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var movieObserver: Observer<Resource<MovieEntity>>


    @Before
    fun setUp() {
        viewModel = DetailMovieViewModel(movieRepository)
        viewModel.selectedMovie(movieId)
    }

    @Test
    fun `setSelectedMovie should be success`() {
        val movieResourcesLiveData = MutableLiveData<Resource<MovieEntity>>().apply {
            value = Resource.success(dataMovie)
        }
        `when`(movieRepository.getMoviesWithDetail(movieId)).thenReturn(movieResourcesLiveData)
        viewModel.movieWithDetail.observeForever(movieObserver)
        verify(movieObserver).onChanged(viewModel.movieWithDetail.value)
        val movieResources = movieResourcesLiveData.value
        verify(movieRepository).getMoviesWithDetail(movieId)
        assertEquals(Status.SUCCESS, movieResources?.status)
        assertEquals(dataMovie, movieResources?.data)
        assertEquals(null, movieResources?.message)
        assertEquals(false, movieResources?.data?.favorite)
    }

    @Test
    fun `setFavorite should be success trigger`() {
        val movieResourcesLiveData = MutableLiveData<Resource<MovieEntity>>().apply {
            value = Resource.success(dataMovie.also { it.favorite = true })
        }
        `when`(movieRepository.getMoviesWithDetail(movieId)).thenReturn(movieResourcesLiveData)
        viewModel.movieWithDetail.observeForever(movieObserver)
        viewModel.setFavoriteMovie()
        verify(movieObserver).onChanged(movieResourcesLiveData.value)
        verify(movieRepository).setMoviesFavorite(movieResourcesLiveData.value?.data as MovieEntity, false)
        val expectedValue = movieResourcesLiveData.value
        val actualValue = viewModel.movieWithDetail.value
        assertEquals(expectedValue, actualValue)
        println("ini nilai expect $expectedValue")
        println("ini nilai actual $actualValue")
    }
}