package com.abifarhan.moviecatalogue.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.abifarhan.moviecatalogue.data.source.local.LocalDataSource
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.data.source.remote.RemoteDataSource
import com.abifarhan.moviecatalogue.ui.PagedMovieListUtils
import com.abifarhan.moviecatalogue.utils.AppExecutors
import com.abifarhan.moviecatalogue.utils.DataMovie
import com.abifarhan.moviecatalogue.utils.DataMovie.generateMovieData
import com.abifarhan.moviecatalogue.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.util.concurrent.Executor

class MovieRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)
    private val movieRepository = FakeMovieRepository(remote, local, appExecutors)

    private val movieResponse = DataMovie.generateRemoteMovieData()
    private val movieId = movieResponse[0].id


    @Test
    fun getAllMovies() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getAllMovies()).thenReturn(dataSourceFactory)
        movieRepository.getAllMovies()

        val movieEntities =
            Resource.success(PagedMovieListUtils.mockPagedList(generateMovieData()))
        verify(local).getAllMovies()
        assertNotNull(movieEntities.data)
        assertEquals(
            movieResponse.size.toLong(),
            movieEntities.data?.size?.toLong()
        )
    }


    @Test
    fun getMoviesWithDetail() {
        val dummyMovieEntity = MutableLiveData<MovieEntity>().apply {
            value = generateMovieData()[0]
        }
        `when`(local.getMovieWithDetail(movieId)).thenReturn(dummyMovieEntity)

        val movieEntity = LiveDataTestUtil.getValue(
            movieRepository.getMoviesWithDetail(movieId)
        )
        verify(local).getMovieWithDetail(movieId)
        assertNotNull(movieEntity.data)
        assertNotNull(movieEntity.data?.titleMovie)
        assertEquals(movieResponse[0].originalTitle, movieEntity.data?.titleMovie)
    }


    @Test
    fun getMoviesFavorite() {
        val dataSourceFactory =
            mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MovieEntity>
        `when`(local.getFavoriteMovie()).thenReturn(dataSourceFactory)
        movieRepository.getMoviesFavorite()

        val movieEntities =
            Resource.success(PagedMovieListUtils.mockPagedList(generateMovieData()))
        verify(local).getFavoriteMovie()
        assertNotNull(movieEntities)
        assertEquals(movieResponse.size.toLong(), movieEntities.data?.size?.toLong())
    }

    @Test
    fun setMoviesFavorite() {
        val dummyMovie = generateMovieData()[0]
        `when`(appExecutors.diskIO()).thenReturn(Executor {
            local.setMovieFavorite(dummyMovie, !dummyMovie.favorite)
        })
        movieRepository.setMoviesFavorite(
            dummyMovie,
            !dummyMovie.favorite
        )
        verify(appExecutors).diskIO()
        verify(local).setMovieFavorite(dummyMovie, !dummyMovie.favorite)
    }
}