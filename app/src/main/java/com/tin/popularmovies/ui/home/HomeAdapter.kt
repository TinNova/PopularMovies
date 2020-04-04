package com.tin.popularmovies.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tin.popularmovies.R
import com.tin.popularmovies.api.models.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.view.*

class HomeAdapter(private val onMovieClicked: (Movie, CardView) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.MovieHolder>() {

    private var movies: List<Movie> = listOf()

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {

        val movie = movies[position]

        Picasso.get().load(movie.poster_path).into(holder.itemView.movie_image)

        /*
         * Giving each item a unique transitionName by using the movie.id
         */
        ViewCompat.setTransitionName(holder.itemView.movie_image, movie.id.toString());
        holder.itemView.setOnClickListener { onMovieClicked(movie, holder.itemView.movie_card) }
    }

    fun setData(data: List<Movie>) {
        movies = data
        notifyDataSetChanged()
    }

    class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
