package com.tin.popularmovies.ui.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.squareup.picasso.Picasso
import com.tin.popularmovies.ItemDecorator
import com.tin.popularmovies.R
import com.tin.popularmovies.ViewModelFactory
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.showToast
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_ID
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_TRANSITION
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject


class DetailActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<DetailViewModel>
    private lateinit var viewModel: DetailViewModel
    private val castAdapter = CastAdapter()
    private val trailerAdapter: TrailerAdapter by lazy {
        TrailerAdapter {
            playTrailer(it)
        }
    }

    private var isSavedInCloud: Boolean = false
    private lateinit var favouriteMenu: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        AndroidInjection.inject(this)
        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        intent.extras?.let {
            val movie = it.get(MOVIE_ID) as Movie
            viewModel.onViewLoaded(movie)
            movie_card.transitionName = it.get(MOVIE_TRANSITION) as String
            Picasso.get().load(movie.poster_path).into(movie_image)
            Picasso.get().load(movie.backdrop_path).into(backdrop_image)
            movie_title.text = movie.title
            movie_rating.text = movie.vote_average.toString()
            movie_release_date.text = movie.release_date
            movie_synopsis.text = movie.overview
            setCollapsingToolbarTitle(movie.title)
        }

        setupRecyclerView()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer<DetailViewState> {
            it?.let {
                when (it.isPresenting) {
                    true -> showData(it.detailData)
//                    false -> movie_recycler_view.gone()
                }
                when (it.isLoading) {
//                    true -> loading_icon.visible()
//                    false -> loading_icon.gone()
                }
                when (it.isNetworkError) {
//                    true -> network_error.visible()
//                    false -> network_error.gone()
                }
                when (it.isSavedInCloud) {
                    true -> {
                        isSavedInCloud = true
                        favouriteMenu.setIcon(R.drawable.ic_favorite_white_24dp)
                    }
                    false -> {
                        isSavedInCloud = false
                        favouriteMenu.setIcon(R.drawable.ic_favorite_border_white_24dp)
                    }
                }
                when (it.toastMessage) {
                    null -> Log.d("ignore", "ignore")
                    else -> this.showToast(getString(it.toastMessage))
                }
            }
        })
    }

    private fun showData(it: DetailData) {
        castAdapter.setData(it.cast)
        trailerAdapter.setData(it.trailers)
    }

    private fun playTrailer(trailerUrl: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = trailerUrl

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            this.showToast(getString(R.string.trailer_play_failed))
        }
    }

    private fun setupRecyclerView() {
        cast_recyclerView.adapter = castAdapter
        setupLinearLayout(cast_recyclerView)
        trailer_recyclerview.adapter = trailerAdapter
        setupLinearLayout(trailer_recyclerview)
        collapsing_toolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
    }

    private fun setupLinearLayout(recyclerview: RecyclerView) {
        recyclerview.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            val itemDecorator = object : ItemDecorator(R.dimen.margin_default) {}
            addItemDecoration(itemDecorator)
            ViewCompat.setNestedScrollingEnabled(this, false)
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        favouriteMenu = menu.findItem(R.id.favourite_icon)
        observeViewState()
        viewModel.onCreateOptionsMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            R.id.favourite_icon -> {
                if (isSavedInCloud) {
                    isSavedInCloud = false
                    viewModel.onIconClickedDeleteMovie()
                    favouriteMenu.setIcon(R.drawable.ic_favorite_border_white_24dp)
                } else {
                    // icon only changes if movie saved successfully
                    viewModel.onIconClickedSaveMovie()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCollapsingToolbarTitle(title: String) {
        var isShow = true
        var scrollRange = -1
        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                collapsing_toolbar.title = title
                isShow = true
            } else if (isShow) {
                collapsing_toolbar.title = " "
                isShow = false
            }
        })
    }
}
