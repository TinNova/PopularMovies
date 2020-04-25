package com.tin.popularmovies.ui.home

import androidx.cardview.widget.CardView
import com.tin.popularmovies.api.models.Movie

interface MovieClickListener {

    fun onMovieClick(clickedMovie: Movie, movieCard: CardView)
}