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
