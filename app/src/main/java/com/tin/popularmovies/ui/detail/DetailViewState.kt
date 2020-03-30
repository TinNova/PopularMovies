package com.tin.popularmovies.ui.detail

data class DetailViewState(
    val detailData: DetailData = DetailData(null, emptyList(), emptyList()),
    val isPresenting: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false
)