package com.tin.popularmovies.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tin.popularmovies.R
import com.tin.popularmovies.api.models.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(
        movie: Movie,
        moviePositionListener: MovieClickListener
    ) {

        Picasso.get()
            .load(movie.poster_path)
            .placeholder(R.drawable.ic_favorite_white_24dp)
            .into(itemView.movie_image)

        itemView.setOnClickListener {
            moviePositionListener.onMovieClick(movie, itemView.movie_card)
        }
    }
}
