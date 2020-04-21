package com.tin.popularmovies.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tin.popularmovies.DisposingViewModel
import com.tin.popularmovies.R
import com.tin.popularmovies.api.TheMovieDbRepo
import com.tin.popularmovies.api.models.Movie
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val theMovieDbRepo: TheMovieDbRepo
) : DisposingViewModel() {

    val viewState = MutableLiveData<DetailViewState>()

    private lateinit var movie: Movie
    private lateinit var mDocRef: DocumentReference

    fun onViewLoaded(movie: Movie) {
        this.movie = movie
        val movieUid = movie.id
        val userUid = FirebaseAuth.getInstance().currentUser?.uid

        mDocRef = FirebaseFirestore.getInstance()
            .document("${USERS_COLLECTION_KEY}/$userUid/${MOVIES_COLLECTION_KEY}/$movieUid")

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

    fun saveMovieToCloud() {

        val movieToSave = hashMapOf(
            "backdrop_path" to movie.backdrop_path,
            "id" to movie.id,
            "overview" to movie.overview,
            "poster_path" to movie.poster_path,
            "title" to movie.title,
            "release_date" to movie.release_date,
            "vote_average" to movie.vote_average
        )

        mDocRef.set(movieToSave).addOnSuccessListener {

            viewState.value = DetailViewState(
                isSavedInCloud = true,
                toastMessage = R.string.saved_cloud // should create ContextWrapper Class for this
            )
            Log.d("Movie", "Document has been saved!")
        }.addOnFailureListener {
            viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.save_failed_cloud)
            Log.d("Movie", "Document save failed!")
        }

    }

    fun checkIfSavedInCloud() {

        mDocRef.get().addOnSuccessListener {

            if (it.exists()) {
                viewState.value = DetailViewState(isSavedInCloud = true)
                Log.d("Movie", "Document Retrieved")
            }
        }.addOnFailureListener {

            Log.d("Movie", "Document Failed To Retrieve")
        }
    }

    fun deleteMovie() {

        mDocRef.delete().addOnSuccessListener {

            viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.deleted_cloud)
            Log.d("Movie", "Document Deleted")

        }.addOnFailureListener {

            viewState.value = DetailViewState(isSavedInCloud = false, toastMessage = R.string.delete_failed_cloud)
            Log.d("Movie", "Document Failed To Delete")

        }
    }

    companion object {
        const val USERS_COLLECTION_KEY = "Users"
        const val MOVIES_COLLECTION_KEY = "Movies"
    }
}
