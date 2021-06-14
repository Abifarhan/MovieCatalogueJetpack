package com.abifarhan.moviecatalogue.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movieentities")
data class MovieEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieId")
    var movieId: String,
    @ColumnInfo(name = "title")
    var titleMovie: String,
    @ColumnInfo(name = "image")
    var imageMovie: String,
    @ColumnInfo(name = "description")
    var descriptionMovie: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false
) : Parcelable
