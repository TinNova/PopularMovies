package com.tin.popularmovies.ui.detail

import androidx.annotation.StringRes
import com.tin.popularmovies.api.models.Detail

data class DetailViewState(
    val detailData: DetailData = DetailData(Detail(), emptyList(), emptyList()),
    val isPresenting: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val isSavedInCloud: Boolean = false,
    @StringRes val toastMessage: Int?  = null
)
