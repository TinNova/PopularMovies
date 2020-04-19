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
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_ID
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_TRANSITION
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.internal.wait
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
    private lateinit var movie: Movie
    private lateinit var mDocRef: DocumentReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        intent.extras?.let {
            movie = it.get(MOVIE_ID) as Movie
            viewModel.onViewLoaded(movie.id)
            movie_card.transitionName = it.get(MOVIE_TRANSITION) as String
            Picasso.get().load(movie.poster_path).into(movie_image)
            Picasso.get().load(movie.backdrop_path).into(backdrop_image)
            movie_title.text = movie.title
            movie_rating.text = movie.vote_average.toString()
            movie_release_date.text = movie.release_date
            movie_synopsis.text = movie.overview
            setCollapsingToolbarTitle(movie.title)
        }

        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observeViewState()
        setupRecyclerView()

        fetchButton.setOnClickListener {
            fetchMovie()
        }

        deleteButton.setOnClickListener {
            deleteMovie()
        }


        val userUid = FirebaseAuth.getInstance().currentUser?.uid

        val movieUid =  movie.id

        mDocRef = FirebaseFirestore.getInstance().document("$USERS_COLLECTION_KEY/$userUid/$MOVIES_COLLECTION_KEY/$movieUid")

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
            Toast.makeText(this, "Can't Play Trailer", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        cast_recyclerView.adapter = castAdapter
        setupLinearLayout(cast_recyclerView)
        trailer_recyclerview.adapter = trailerAdapter
        setupLinearLayout(trailer_recyclerview)

        collapsing_toolbar.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            R.id.favourite -> {
                saveMovieToFireCloud()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMovieToFireCloud() {


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
            Log.d("Movie", "Document has been saved!")
        }.addOnFailureListener {
            Log.d("Movie", "Document was not saved!")
        }

    }

    private fun fetchMovie() {

        mDocRef.get().addOnSuccessListener {
            if (it.exists()) {

                it.toObject<Movie>()?.let {

                    val savedMovie: Movie = it
                    movie_synopsis.text = savedMovie.title

                    val title = savedMovie.title

                    Log.d("Movie", "Document Retrieved: $title")
                }
            }
        }.addOnFailureListener {

            Log.d("Movie", "Document Failed To Retrieve")

        }
    }

    private fun deleteMovie() {

        mDocRef.delete().addOnSuccessListener {

            Log.d("Movie", "Document Deleted")

        }.addOnFailureListener {

            Log.d("Movie", "Document Failed To Delete")

        }
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

    companion object {
        const val MOVIE_DATA_KEY = "movie"
        const val USERS_COLLECTION_KEY = "Users"
        const val MOVIES_COLLECTION_KEY = "Movies"
    }
}
