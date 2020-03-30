package com.tin.popularmovies.api.models

import com.google.gson.annotations.SerializedName


data class TrailersResult(
    val id: Int,
    @SerializedName("results")
    val trailers: List<Trailer> = emptyList()
)

data class Trailer(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)

//TODO: This should be done within the Repo, add another value to the Trailer to allow us to take
// the trailer from the model
/** HOW THE THUMBNAIL IMAGE IS CONSTRUCTED
 * String thumbnailImage = "https://img.youtube.com/vi/" + trailers.getTrailerKey + "/0.jpg";
 */
private val YOUTUBETHUMBNAILSTART: String? = "https://img.youtube.com/vi/"
private val YOUTUBETHUMBNAILEND: String = "/0.jpg"
private val YOUTUBE_TRAILER_BASE_URL = "https://www.youtube.com/watch?v="
