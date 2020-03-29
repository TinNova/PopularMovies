package com.tin.popularmovies.ui.detail

import androidx.lifecycle.MutableLiveData
import com.tin.popularmovies.DisposingViewModel
import com.tin.popularmovies.TheMovieDbRepo
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val theMovieDbRepo: TheMovieDbRepo
) : DisposingViewModel() {

    val viewState = MutableLiveData<DetailViewState>()

    fun onViewLoaded(movieId: Int) {
        getData(movieId)
    }

    private fun getData(movieId: Int) {
        add(theMovieDbRepo.getDetailData(movieId).subscribe(
            {
                viewState.value =
                    DetailViewState(
                        detailData = it,
                        isPresenting = true,
                        isLoading = false,
                        isNetworkError = false
                    )
            }, {
                viewState.value =
                    DetailViewState(
                        isPresenting = false,
                        isLoading = false,
                        isNetworkError = true
                    )
            }
        ))
    }

}