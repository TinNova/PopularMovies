package com.tin.popularmovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tin.popularmovies.R
import com.tin.popularmovies.ViewModelFactory
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.gone
import com.tin.popularmovies.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<HomeViewModel>
    private lateinit var viewModel: HomeViewModel

    private val adapter: HomeAdapter by lazy { HomeAdapter { launchDetailActivity(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        setupRecyclerView()

        viewModel.onViewLoaded()
        observeViewState()
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

    private fun launchDetailActivity(it: Movie) {
        println("movie9090 ${it.title}")
    }

    private fun setupRecyclerView() {
        movie_recycler_view.setHasFixedSize(true)
        movie_recycler_view.layoutManager = GridLayoutManager(this, 2)
        movie_recycler_view.adapter = adapter
    }
}
