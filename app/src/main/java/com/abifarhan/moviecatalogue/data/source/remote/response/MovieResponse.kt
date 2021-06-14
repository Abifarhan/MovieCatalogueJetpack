package com.abifarhan.moviecatalogue.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    var id: String,
    var originalTitle: String,
    var posterPath: String,
    var overview: String
) : Parcelable