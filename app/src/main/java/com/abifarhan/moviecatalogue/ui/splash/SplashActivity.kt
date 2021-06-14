package com.abifarhan.moviecatalogue.ui.splash

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.abifarhan.moviecatalogue.R
import com.abifarhan.moviecatalogue.databinding.ActivitySplashBinding
import com.abifarhan.moviecatalogue.ui.home.HomeActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        Glide.with(this)
            .load(R.drawable.film)
            .centerCrop()
            .apply(RequestOptions.overrideOf(600, 600))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(
                        this@SplashActivity,
                        "Gagal mengupload Gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Palette.from(resource.toBitmap())
                        .generate {
                            val intColor = it?.vibrantSwatch?.rgb ?: 0
                            mBinding.splashContainer.setBackgroundColor(intColor)
                        }
                    return false
                }
            })
            .into(mBinding.imageViewSplash)

        mBinding.imageViewSplash.animate().setDuration(2000)
            .alpha(1f).withEndAction{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
    }
}