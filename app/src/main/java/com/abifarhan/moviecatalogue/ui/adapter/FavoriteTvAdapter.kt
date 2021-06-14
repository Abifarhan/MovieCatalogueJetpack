package com.abifarhan.moviecatalogue.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.databinding.ItemsFavoriteBinding
import com.abifarhan.moviecatalogue.ui.detail.tvshow.DetailTvShowActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteTvAdapter(private val callback: FavoriteFragmentCallback) :
    PagedListAdapter<TvShowEntity, FavoriteTvAdapter.TvViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TvShowEntity>() {
            override fun areItemsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem.tvShowId == newItem.tvShowId
            }

            override fun areContentsTheSame(oldItem: TvShowEntity, newItem: TvShowEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class TvViewHolder(private val binding: ItemsFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tv: TvShowEntity) {
            with(binding) {
                tvItemTitleMovieFavMovie.text = tv.titleTv

                Glide.with(itemView.context)
                    .load(tv.imageTv)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgMovieFavMovie)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailTvShowActivity::class.java)
                    intent.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tv.tvShowId)
                    itemView.context.startActivity(intent)
                }

                imgShare.setOnClickListener {
                    callback.onShareClick(tv)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        val itemsFavoriteBinding =
            ItemsFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvViewHolder(itemsFavoriteBinding)
    }

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        val tv = getItem(position)
        if (tv != null) {
            holder.bind(tv)
        }
    }

    interface FavoriteFragmentCallback {
        fun onShareClick(movie: TvShowEntity)
    }

    fun getSwipedData(swipedPosition: Int): TvShowEntity? = getItem(swipedPosition)
}