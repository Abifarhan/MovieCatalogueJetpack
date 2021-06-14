package com.abifarhan.moviecatalogue.ui.tvshow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.abifarhan.moviecatalogue.databinding.FragmentTvShowBinding
import com.abifarhan.moviecatalogue.ui.adapter.TvShowAdapter
import com.abifarhan.moviecatalogue.ui.tvshow.viewmodel.ViewModelTvFactory
import com.abifarhan.moviecatalogue.vo.Status

class TvShowFragment : Fragment() {

    private var _fragmentTvShowBinding: FragmentTvShowBinding? = null
    private val binding get() = _fragmentTvShowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentTvShowBinding = FragmentTvShowBinding.inflate(layoutInflater)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val factory = ViewModelTvFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[TvShowViewModel::class.java]
            val tvShowAdapter = TvShowAdapter()
            viewModel.getTvShow().observe(viewLifecycleOwner, { tv ->
                if (tv != null) {
                    when (tv.status) {
                        Status.LOADING -> binding?.progressBarTvShow?.visibility = View.VISIBLE

                        Status.SUCCESS -> {
                            binding?.progressBarTvShow?.visibility = View.GONE
                            tvShowAdapter.submitList(tv.data)
                        }

                        Status.ERROR -> {
                            binding?.progressBarTvShow?.visibility = View.GONE
                            Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(binding?.rvTvShow) {
                this?.layoutManager = GridLayoutManager(context, 2)
                this?.setHasFixedSize(true)
                this?.adapter = tvShowAdapter
            }
        }
    }
}