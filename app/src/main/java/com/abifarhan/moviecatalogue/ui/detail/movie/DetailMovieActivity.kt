package com.abifarhan.moviecatalogue.ui.detail.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.data.source.local.entity.MovieEntity
import com.abifarhan.moviecatalogue.databinding.ActivityDetailMovieBinding
import com.abifarhan.moviecatalogue.databinding.ContentDetailMovieBinding
import com.abifarhan.moviecatalogue.ui.movie.viewmodel.ViewModelFactory
import com.abifarhan.moviecatalogue.vo.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var activityDetailMovieBinding: ActivityDetailMovieBinding
    private lateinit var detailContentBinding: ContentDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel
    private var menu: Menu? = null

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailMovieBinding = ActivityDetailMovieBinding.inflate(layoutInflater)
        detailContentBinding = activityDetailMovieBinding.detailContent
        setContentView(activityDetailMovieBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailMovieViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            val movieId = extras.getString(EXTRA_MOVIE)
            if (movieId != null) {
                viewModel.selectedMovie(movieId)
                viewModel.movieWithDetail.observe(this, {
                    if (it != null) {
                        when (it.status) {
                            Status.LOADING -> activityDetailMovieBinding.progressBar.visibility =
                                View.VISIBLE
                            Status.SUCCESS -> if (it.data != null) {
                                activityDetailMovieBinding.progressBar.visibility = View.GONE
                                activityDetailMovieBinding.nestedScrollView.visibility =
                                    View.VISIBLE
                                populateMovie(it.data)
                            }
                            Status.ERROR -> {
                                activityDetailMovieBinding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }

    }

    private fun populateMovie(movieEntity: MovieEntity) {
        detailContentBinding.textViewTitleDetailMovie.text = movieEntity.titleMovie
        detailContentBinding.textViewDescriptionDetailMovie.text = movieEntity.descriptionMovie
        Glide.with(this)
            .load(movieEntity.imageMovie)
            .transform(RoundedCorners(30))
            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
            .error(R.drawable.ic_error)
            .into(detailContentBinding.imageViewDetailMovie)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu

        viewModel.movieWithDetail.observe(this, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> activityDetailMovieBinding.progressBar.visibility =
                        View.VISIBLE
                    Status.SUCCESS -> if (it.data != null) {
                        activityDetailMovieBinding.progressBar.visibility = View.GONE
                        val state = it.data.favorite
                        setFavoriteState(state)
                    }
                    Status.ERROR -> {
                        activityDetailMovieBinding.progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bookmark) {
            viewModel.setFavoriteMovie()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFavoriteState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_done)
        } else {
            menuItem?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_not_yet)
        }
    }
}