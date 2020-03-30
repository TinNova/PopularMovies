package com.tin.popularmovies.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        const val MOVIE_ID = "movieId"
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<HomeViewModel>
    private lateinit var viewModel: HomeViewModel

    private val adapter: HomeAdapter by lazy {
        HomeAdapter {
            launchDetailActivity(it)
        }
    }

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
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(MOVIE_ID, it.id)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        movie_recycler_view.setHasFixedSize(true)
        movie_recycler_view.layoutManager = GridLayoutManager(this, 2)
        movie_recycler_view.adapter = adapter
    }
}