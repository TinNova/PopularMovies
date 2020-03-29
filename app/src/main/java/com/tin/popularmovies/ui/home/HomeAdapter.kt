package com.tin.popularmovies.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tin.popularmovies.Const.BASE_IMAGE_URL
import com.tin.popularmovies.R
import com.tin.popularmovies.api.models.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class HomeAdapter(private val onMovieClicked: (Movie) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.MovieHolder>() {

    private var movies: List<Movie> = listOf()

    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener { onMovieClicked(movies[position]) }
    }

    fun setData(data: List<Movie>) {
        movies = data
        notifyDataSetChanged()
    }

    class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            Picasso.get()
                .load(BASE_IMAGE_URL + movie.poster_path)
                .into(itemView.movieImage);
        }
    }
}
