package com.abifarhan.moviecatalogue.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tventities")
data class TvShowEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tvId")
    val tvShowId: String,
    @ColumnInfo(name = "title")
    val titleTv: String,
    @ColumnInfo(name = "image")
    val imageTv: String,
    @ColumnInfo(name = "description")
    val descriptionTv: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false
) : Parcelable
