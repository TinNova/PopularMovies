package com.tin.popularmovies.ui.detail

import com.tin.popularmovies.api.models.Cast
import com.tin.popularmovies.api.models.Detail
import com.tin.popularmovies.api.models.Trailer

data class DetailData(
    val detail: Detail?,
    val trailers: List<Trailer>,
    val cast: List<Cast>
)