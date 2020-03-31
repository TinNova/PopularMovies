package com.tin.popularmovies.ui.detail

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tin.popularmovies.Const.YOUTUBE_THUMBNAIL_END_URL
import com.tin.popularmovies.Const.YOUTUBE_THUMBNAIL_START_URL
import com.tin.popularmovies.R
import com.tin.popularmovies.api.models.Trailer
import com.tin.popularmovies.ui.detail.TrailerAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_trailer.view.*

class TrailerAdapter(private val playTrailer: (Uri) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    private var trailers: List<Trailer> = listOf()

    override fun getItemCount(): Int = trailers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_trailer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(trailers[pos])
        holder.itemView.trailer_thumbnail.setOnClickListener { playTrailer(trailers[pos].trailerUrl) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(trailer: Trailer) {

            itemView.trailer_title.text = trailer.name

            Picasso.get()
                .load(YOUTUBE_THUMBNAIL_START_URL + trailer.key + YOUTUBE_THUMBNAIL_END_URL)
                .into(itemView.trailer_thumbnail)
        }
    }

    fun setData(data: List<Trailer>) {
        trailers = data
        notifyDataSetChanged()
    }
}