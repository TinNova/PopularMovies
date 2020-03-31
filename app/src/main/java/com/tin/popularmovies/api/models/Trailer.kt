package com.tin.popularmovies.api.models

import android.net.Uri
import com.google.gson.annotations.SerializedName
import com.tin.popularmovies.Const


data class TrailersResult(
    val id: Int,
    @SerializedName("results")
    val trailers: List<TrailerOriginal> = emptyList()
)

data class TrailerOriginal(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)

data class Trailer(
    val id: String,
    val key: String,
    val thumbnail: String,
    val trailerUrl: Uri,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)

fun TrailerOriginal.returnCleanTrailer() =
    Trailer(
        id = id,
        key = key,
        thumbnail = Const.YOUTUBE_THUMBNAIL_START_URL + key + Const.YOUTUBE_THUMBNAIL_END_URL,
        trailerUrl = Uri.parse(Const.YOUTUBE_TRAILER_BASE_URL + key),
        name = name,
        site = site,
        size = size,
        type = type
    )
