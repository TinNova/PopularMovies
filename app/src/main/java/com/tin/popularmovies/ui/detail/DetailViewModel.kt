package com.tin.popularmovies.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tin.popularmovies.DisposingViewModel
import com.tin.popularmovies.R
import com.tin.popularmovies.api.FireCloud
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.room.mapToMovieSql
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val theMovieDbRepo: TheMovieDbRepo,
    private val fireCloud: FireCloud
) : DisposingViewModel() {

    val viewState = MutableLiveData<DetailViewState>()

    private lateinit var movie: Movie
    private lateinit var movieDocument: DocumentReference
    private var isLoggedIn = false

    fun onViewLoaded(movie: Movie) {
        this.movie = movie
        val movieUid = movie.id

        if (fireCloud.isUserLoggedIn()) {
            isLoggedIn = true
            movieDocument = fireCloud.getMovieDocument(movieUid)
        } else {
            isLoggedIn = false
        }

        getData(movieUid)
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

    fun onCreateOptionsMenu() {
        if (isLoggedIn) getMovieFromCloud() else getMovieFromRoom()
    }

    fun onIconClickedSaveMovie() {

        if (isLoggedIn) saveMovieToCloud() else saveMovieToRoom()
    }

    fun onIconClickedDeleteMovie() {

        if (isLoggedIn) deleteMovieFromCloud() else deleteMovieFromRoom()
    }

    private fun getMovieFromCloud() {

        movieDocument.get().addOnSuccessListener {

            if (it.exists()) {
                viewState.value = DetailViewState(isSavedInCloud = true)
                Log.d("Movie", "Document Retrieved")
            }
        }.addOnFailureListener {

            Log.d("Movie", "Document Failed To Retrieve")
        }
    }

    private fun saveMovieToCloud() {
        val movieToSave = hashMapOf(
            "backdrop_path" to movie.backdrop_path,
            "id" to movie.id,
            "overview" to movie.overview,
            "poster_path" to movie.poster_path,
            "title" to movie.title,
            "release_date" to movie.release_date,
            "vote_average" to movie.vote_average
        )

        movieDocument.set(movieToSave).addOnSuccessListener {

            viewState.value = DetailViewState(
                isSavedInCloud = true,
                toastMessage = R.string.saved_cloud
            )
            Log.d("Movie", "Document has been saved!")
        }.addOnFailureListener {
            viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.save_failed_cloud)
            Log.d("Movie", "Document save failed!")
        }
    }

    private fun deleteMovieFromCloud() {
        movieDocument.delete().addOnSuccessListener {

            viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.deleted_cloud)
            Log.d("Movie", "Document Deleted")

        }.addOnFailureListener {

            viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.delete_failed_cloud)
            Log.d("Movie", "Document Failed To Delete")

        }
    }

    private fun getMovieFromRoom() {

        add(theMovieDbRepo.getMovieById(movie.id)
            .subscribe(
                {
                    viewState.value = DetailViewState(isSavedInCloud = true)
                    Log.d("Movie", "Retrieved From Room")
                }, {
                    Log.d("Movie", "Retrieve Failed From Room")
                })
        )
    }

    private fun saveMovieToRoom() {

        add(theMovieDbRepo.insertMovie(movie.mapToMovieSql())
            .subscribe(
                {
                    viewState.value = DetailViewState(
                        isSavedInCloud = true,
                        toastMessage = R.string.saved_locally
                    )
                    Log.d("Movie", "Saved To Room")
                }, {
                    viewState.value =
                        DetailViewState(isSavedInCloud = false, toastMessage = R.string.save_failed_locally)
                    Log.d("Movie", "Save To Room Failed")
                })
        )
    }

    private fun deleteMovieFromRoom() {
        add(
            theMovieDbRepo.deleteMovie(movie.mapToMovieSql())
                .subscribe({
                    viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.deleted_locally)
                    Log.d("Movie", "Deleted From Room")
                }, {
                    viewState.value =
                        DetailViewState(isSavedInCloud = false, toastMessage = R.string.delete_failed_locally)
                    Log.d("Movie", "Document Failed To Delete From Room")
                })
        )
    }
}
