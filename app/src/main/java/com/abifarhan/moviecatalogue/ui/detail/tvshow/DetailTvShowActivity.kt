package com.abifarhan.moviecatalogue.ui.detail.tvshow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.databinding.ActivityDetailTvShowBinding
import com.abifarhan.moviecatalogue.databinding.ContentDetailTvShowBinding
import com.abifarhan.moviecatalogue.ui.tvshow.viewmodel.ViewModelTvFactory
import com.abifarhan.moviecatalogue.vo.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class DetailTvShowActivity : AppCompatActivity() {
    private lateinit var activityDetailTvShowBinding: ActivityDetailTvShowBinding
    private lateinit var detailTvShowBinding: ContentDetailTvShowBinding
    private lateinit var viewModel: DetailTvShowViewModel
    private var menu: Menu? = null

    companion object {
        const val EXTRA_TV_SHOW = "extra_tv_show"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailTvShowBinding = ActivityDetailTvShowBinding.inflate(layoutInflater)
        detailTvShowBinding = activityDetailTvShowBinding.detailContentTv
        setContentView(activityDetailTvShowBinding.root)

        val factory = ViewModelTvFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[DetailTvShowViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            val tvId = extras.getString(EXTRA_TV_SHOW)
            if (tvId != null) {
                viewModel.selectedTv(tvId)
                viewModel.tvWithDetail.observe(this, {
                    if (it != null) {
                        when (it.status) {
                            Status.LOADING -> activityDetailTvShowBinding.progressBar.visibility =
                                View.VISIBLE

                            Status.SUCCESS -> if (it.data != null) {
                                activityDetailTvShowBinding.progressBar.visibility = View.GONE
                                activityDetailTvShowBinding.nestedScrollView.visibility =
                                    View.VISIBLE
                                populateTvShow(it.data)
                            }

                            Status.ERROR -> {
                                activityDetailTvShowBinding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun populateTvShow(tvShowEntity: TvShowEntity) {
        detailTvShowBinding.apply {
            textViewTitleDetailTvShow.text = tvShowEntity.titleTv
            textViewDescriptionDetailTvShow.text = tvShowEntity.descriptionTv
        }
        Glide.with(this)
            .load(tvShowEntity.imageTv)
            .transform(RoundedCorners(30))
            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
            .error(R.drawable.ic_error)
            .into(detailTvShowBinding.imageViewDetailTvShow)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_tv, menu)
        this.menu = menu
        viewModel.tvWithDetail.observe(this, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> activityDetailTvShowBinding.progressBar.visibility =
                        View.VISIBLE

                    Status.SUCCESS -> if (it.data != null) {
                        activityDetailTvShowBinding.progressBar.visibility = View.GONE
                        val state = it.data.favorite
                        setFavoriteState(state)
                    }

                    Status.ERROR -> {
                        activityDetailTvShowBinding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bookmark_tv) {
            viewModel.setFavoritTv()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFavoriteState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark_tv)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_done)
        } else {
            menuItem?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_not_yet)
        }
    }
}