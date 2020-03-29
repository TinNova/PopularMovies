package com.tin.popularmovies.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tin.popularmovies.R
import com.tin.popularmovies.ViewModelFactory
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_ID
import com.tin.popularmovies.ui.home.HomeViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<DetailViewModel>
    private lateinit var viewModel: DetailViewModel

    private var castAdapter = CastAdapter()
//    private lateinit var trailerAdapter: TrailerAdapter

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
    }

    // Add View Decorators here to add spacing between cast images
    private fun setupRecyclerView() {
        cast_recyclerView.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            setHasFixedSize(true)
        }
    }

}
