package com.abifarhan.moviecatalogue.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abifarhan.moviecatalogue.data.source.local.entity.TvShowEntity


@Database(
    entities = [TvShowEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TvDatabase : RoomDatabase() {
    abstract fun tvDao(): TvDao

    companion object {
        @Volatile
        private var INSTANCE: TvDatabase? = null

        fun getInstance(context: Context): TvDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TvDatabase::class.java,
                    "Tv.db"
                ).build().apply {
                    INSTANCE = this
                }
            }

    }
}