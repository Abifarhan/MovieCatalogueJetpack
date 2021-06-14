package com.abifarhan.moviecatalogue.ui.tvshow.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abifarhan.moviecatalogue.data.TvRepository
import com.abifarhan.moviecatalogue.ui.detail.tvshow.DetailTvShowViewModel
import com.abifarhan.moviecatalogue.ui.favorit.tv.FavoriteTvViewModel
import com.abifarhan.moviecatalogue.ui.tvshow.TvInjection
import com.abifarhan.moviecatalogue.ui.tvshow.TvShowViewModel

class ViewModelTvFactory private constructor(private val mTvRepository: TvRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelTvFactory? = null

        fun getInstance(context: Context): ViewModelTvFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelTvFactory(TvInjection.provideRepository(context)).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TvShowViewModel::class.java) -> {
                TvShowViewModel(mTvRepository) as T
            }
            modelClass.isAssignableFrom(DetailTvShowViewModel::class.java) -> {
                DetailTvShowViewModel(mTvRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteTvViewModel::class.java) -> {
                FavoriteTvViewModel(mTvRepository) as T
            }
            else -> throw  Throwable("Unknown ViewModel class : " + modelClass.name)
        }
    }
}