package com.tin.popularmovies.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.tin.popularmovies.ui.login.LoginActivity
import com.tin.popularmovies.visible
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), MovieClickListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<HomeViewModel>
    private lateinit var viewModel: HomeViewModel

    private val networkAdapter = HomeNetworkAdapter(this)
    private val savedAdapter = HomeSavedAdapter(this)

    private var isShowingSaved = false
    private var isUserLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        setupRecyclerView()
        viewModel.onViewLoaded()
        observePaging()
        observeViewState()

        error_btn.setOnClickListener {
            viewModel.retryGetMovies()
        }
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer<HomeViewState> {
            it?.let {
                when (it.isShowingSaved) {
                    true -> showSavedMovies(it.movies)
                    false -> showNetworkMovies()
                }
                when (it.isLoading) {
//                    true -> loading_icon.visible()
//                    false -> loading_icon.gone()
                }
                when (it.isNetworkError) {
                    true -> showErrorMessage()
                    false -> hideErrorMessage()
                }
                when (it.isSigningOut) {
                    true -> navigateToLoginActivity()
                }
                when (it.isUserLoggedIn) {
                    true -> isUserLoggedIn = true
                    false -> isUserLoggedIn = false
                }
            }
        })
    }

    private fun observePaging() {
        viewModel.pagedMovieState.observe(this, Observer {
            networkAdapter.submitList(it)
        })
    }

    private fun showErrorMessage() {
        error_text.visible()
        error_btn.visible()
        movie_recycler_view.gone()
    }

    private fun hideErrorMessage() {
        error_text.gone()
        error_btn.gone()
        movie_recycler_view.visible()
    }

    private fun showNetworkMovies() {
        movie_recycler_view.removeAllViewsInLayout()
        movie_recycler_view.adapter = networkAdapter
        isShowingSaved = false
    }

    private fun showSavedMovies(movies: List<Movie>) {
        movie_recycler_view.removeAllViewsInLayout()
        movie_recycler_view.adapter = savedAdapter
        savedAdapter.setData(movies)
        isShowingSaved = true
    }

    private fun setupRecyclerView() {
        movie_recycler_view.setHasFixedSize(true)
        movie_recycler_view.layoutManager = GridLayoutManager(this, 2)
        movie_recycler_view.adapter = networkAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        menu.findItem(R.id.log_out_icon).isVisible = isUserLoggedIn
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favourite_icon -> viewModel.onFavouriteIconClicked(isShowingSaved)
            R.id.log_out_icon -> viewModel.onLogoutIconClicked()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMovieClick(clickedMovie: Movie, movieCard: CardView) {
        launchDetailActivity(clickedMovie, movieCard)
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
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    companion object {
        const val MOVIE_ID = "movie_key"
        const val MOVIE_TRANSITION = "movie_transition"
    }
}
