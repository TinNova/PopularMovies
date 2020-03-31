package com.tin.popularmovies.ui.detail

import com.tin.popularmovies.api.models.Detail

data class DetailViewState(
    val detailData: DetailData = DetailData(Detail(), emptyList(), emptyList()),
    val isPresenting: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false
)
