package com.abifarhan.moviecatalogue.ui.favorit.tv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity
import com.abifarhan.moviecatalogue.databinding.FragmentFavoritTvBinding
import com.abifarhan.moviecatalogue.ui.adapter.FavoriteTvAdapter
import com.abifarhan.moviecatalogue.ui.tvshow.viewmodel.ViewModelTvFactory
import com.google.android.material.snackbar.Snackbar


class FavoriteTvFragment : Fragment(), FavoriteTvAdapter.FavoriteFragmentCallback {

    private var _fragmentFavoriteBinding: FragmentFavoritTvBinding? = null
    private val binding get() = _fragmentFavoriteBinding

    private lateinit var viewModel: FavoriteTvViewModel
    private lateinit var adapter: FavoriteTvAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentFavoriteBinding = FragmentFavoritTvBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemTouchHelper.attachToRecyclerView(binding?.rvFavoriteTv)


        if (activity != null) {
            val factory = ViewModelTvFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[FavoriteTvViewModel::class.java]


            adapter = FavoriteTvAdapter(this)
            binding?.progressBar?.visibility = View.VISIBLE
            viewModel.getTvFavorite().observe(viewLifecycleOwner, {
                binding?.progressBar?.visibility = View.GONE
                adapter.submitList(it)
            })

            binding?.rvFavoriteTv?.layoutManager = LinearLayoutManager(context)
            binding?.rvFavoriteTv?.setHasFixedSize(true)
            binding?.rvFavoriteTv?.adapter = adapter

        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)


        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val movieEntity = adapter.getSwipedData(swipedPosition)
                movieEntity?.let { viewModel.setTvFavorite(it) }

                val snackbar = Snackbar.make(view as View, R.string.undo, Snackbar.LENGTH_LONG)
                snackbar.setAction("OKE") { v ->
                    movieEntity?.let { viewModel.setTvFavorite(it) }
                }
                snackbar.show()
            }
        }

    })

    override fun onShareClick(tv: TvShowEntity) {
        if (activity != null) {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                .from(requireActivity())
                .setType(mimeType)
                .setChooserTitle("Bagikan aplikasi ini sekarang.")
                .setText("Segera nonton film ${tv.titleTv} bioskop kesayangan Anda")
                .startChooser()
        }
    }
}