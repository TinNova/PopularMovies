package com.tin.popularmovies.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tin.popularmovies.R
import com.tin.popularmovies.ViewModelFactory
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.gone
import com.tin.popularmovies.ui.detail.DetailActivity
import com.tin.popularmovies.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID = "movie_key"
        const val MOVIE_TRANSITION = "movie_transition"
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<HomeViewModel>
    private lateinit var viewModel: HomeViewModel

    private val adapter: HomeAdapter by lazy {
        HomeAdapter { movie: Movie, cardView: CardView ->
            launchDetailActivity(movie, cardView)
        }
    }

    // Needed when show list of saved Movies in MainActivity
//    private fun fetchMovie() {
//
//        mDocRef.get().addOnSuccessListener {
//            if (it.exists()) {
//
//                it.toObject<Movie>()?.let {
//
//                    val savedMovie: Movie = it
//                    movie_synopsis.text = savedMovie.title
//
//                    val title = savedMovie.title
//
//                    Log.d("Movie", "Document Retrieved: $title")
//                }
//            }
//        }.addOnFailureListener {
//
//            Log.d("Movie", "Document Failed To Retrieve")
//
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        setupRecyclerView()

        viewModel.onViewLoaded()
        observePaging()
        observeViewState()
    }

    private fun observePaging() {
        viewModel.moviePagedList.observe(this, Observer {
            adapter.submitList(it)
        })
    }


    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer<HomeViewState> {
            it?.let {
                when (it.isPresenting) {
                    true -> showMovies(it.movies)
                    false -> movie_recycler_view.gone()
                }
                when (it.isLoading) {
//                    true -> loading_icon.visible()
//                    false -> loading_icon.gone()
                }
                when (it.isNetworkError) {
//                    true -> network_error.visible()
//                    false -> network_error.gone()
                }
            }
        })
    }

    private fun showMovies(movies: List<Movie>) {
        movie_recycler_view.visible()
        adapter.setData(movies)
    }

    private fun launchDetailActivity(selectedMovie: Movie, cardView: CardView) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(MOVIE_ID, selectedMovie)
        intent.putExtra(MOVIE_TRANSITION, ViewCompat.getTransitionName(cardView));

        // what happens when getTransitionName is null?
        if (ViewCompat.getTransitionName(cardView) == null) {
            startActivity(intent)
        } else {
            startActivity(
                intent,
                makeSceneTransitionAnimation(
                    this,
                    cardView,
                    ViewCompat.getTransitionName(cardView)!! //-> Handle this better
                ).toBundle()
            )
        }

//        ViewCompat.getTransitionName(imageView)?.let {
//            startActivity(intent, makeSceneTransitionAnimation(this, imageView, it).toBundle())
//        }
    }

    private fun setupRecyclerView() {
        movie_recycler_view.setHasFixedSize(true)
        movie_recycler_view.layoutManager = GridLayoutManager(this, 2)
        movie_recycler_view.adapter = adapter
    }
}
