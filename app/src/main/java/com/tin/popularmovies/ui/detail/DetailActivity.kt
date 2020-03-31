package com.tin.popularmovies.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tin.popularmovies.ItemDecorator
import com.tin.popularmovies.R
import com.tin.popularmovies.ViewModelFactory
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_ID
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        intent.extras?.let {
            viewModel.onViewLoaded(it.getInt(MOVIE_ID))
        }

        observeViewState()
        setupRecyclerView()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer<DetailViewState> {
            it?.let {
                when (it.isPresenting) {
                    true -> showData(it)
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

    private fun showData(it: DetailViewState) {
        castAdapter.setData(it.detailData.cast)
        trailerAdapter.setData(it.detailData.trailers)
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
    }

    private fun setupLinearLayout(castRecyclerview: RecyclerView) {
        castRecyclerview.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            val itemDecorator = object : ItemDecorator(R.dimen.margin_default) {}
            addItemDecoration(itemDecorator)
        }
    }
}
