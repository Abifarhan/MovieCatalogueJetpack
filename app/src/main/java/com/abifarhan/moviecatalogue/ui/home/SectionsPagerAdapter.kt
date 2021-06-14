package com.abifarhan.moviecatalogue.ui.home

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.ui.favorit.movie.FavoriteMovieFragment
import com.abifarhan.moviecatalogue.ui.favorit.tv.FavoriteTvFragment
import com.abifarhan.moviecatalogue.ui.movie.MovieFragment
import com.abifarhan.moviecatalogue.ui.tvshow.TvShowFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    companion object {
        @StringRes
        private val TAB_TITLES =
            intArrayOf(R.string.movie, R.string.tv_show, R.string.fav_movie, R.string.fav_tv_show)
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> MovieFragment()
            1 -> TvShowFragment()
            2 -> FavoriteMovieFragment()
            3 -> FavoriteTvFragment()
            else -> Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 4
    }
}