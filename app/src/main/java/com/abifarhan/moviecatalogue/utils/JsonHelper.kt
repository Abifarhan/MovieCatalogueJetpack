package com.abifarhan.moviecatalogue.utils

import android.content.Context
import com.abifarhan.moviecatalogue.data.source.remote.response.MovieResponse
import com.abifarhan.moviecatalogue.data.source.remote.response.TvResponse
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class JsonHelper(private val context: Context) {

    private fun parsingFileToString(fileName: String): String? {
        return try {
            val `is` = context.assets.open(fileName)
            val buffer = ByteArray(`is`.available())
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    fun loadMovie(): List<MovieResponse> {
        val list = ArrayList<MovieResponse>()
        try {
            val responseObject = JSONObject(parsingFileToString("movie.json").toString())
            val listArray = responseObject.getJSONArray("results")
            for (i in 0 until listArray.length()) {
                val movie = listArray.getJSONObject(i)
                val id = movie.getString("id")
                val title = movie.getString("original_title")
                val posterPath = movie.getString("poster_path")
                val overview = movie.getString("overview")

                val movieResponse = MovieResponse(id, title, posterPath, overview)
                list.add(movieResponse)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return list
    }

    fun loadMovieDetail(movieId: String): MovieResponse {
        var dataDetail = MovieResponse("", "", "", "")
        try {
            val responseObject = JSONObject(parsingFileToString("movie.json").toString())
            val listArray = responseObject.getJSONArray("results")
            for (i in 0 until listArray.length()) {
                val movie = listArray.getJSONObject(i)
                val id = movie.getString("id")
                if (movieId == id) {
                    val title = movie.getString("original_title")
                    val posterPath = movie.getString("poster_path")
                    val overview = movie.getString("overview")
                    dataDetail = MovieResponse(id, title, posterPath, overview)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return dataDetail
    }

    fun loadTv(): List<TvResponse> {
        val list = ArrayList<TvResponse>()
        try {
            val responseObject = JSONObject(parsingFileToString("tv.json").toString())
            val listArray = responseObject.getJSONArray("results")
            for (i in 0 until listArray.length()) {
                val tv = listArray.getJSONObject(i)
                val id = tv.getString("id")
                val title = tv.getString("original_name")
                val posterPath = tv.getString("poster_path")
                val overview = tv.getString("overview")

                val tvResponse = TvResponse(id, title, posterPath, overview)
                list.add(tvResponse)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return list
    }

    fun loadTvDetail(tvId: String): TvResponse {
        var dataDetail = TvResponse("", "", "", "")
        try {
            val responseObject = JSONObject(parsingFileToString("tv.json").toString())
            val listArray = responseObject.getJSONArray("results")
            for (i in 0 until listArray.length()) {
                val tv = listArray.getJSONObject(i)
                val id = tv.getString("id")
                if (tvId == id) {
                    val title = tv.getString("original_name")
                    val posterPath = tv.getString("poster_path")
                    val overview = tv.getString("overview")
                    dataDetail = TvResponse(id, title, posterPath, overview)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return dataDetail
    }
}