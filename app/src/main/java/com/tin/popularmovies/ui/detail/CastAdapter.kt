package com.tin.popularmovies.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tin.popularmovies.R
import com.tin.popularmovies.api.models.Cast
import com.tin.popularmovies.ui.detail.CastAdapter.CastViewHolder
import kotlinx.android.synthetic.main.item_cast.view.*

class CastAdapter : RecyclerView.Adapter<CastViewHolder>() {

    private var cast: List<Cast> = listOf()

    override fun getItemCount(): Int = cast.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, pos: Int) {
        holder.bind(cast[pos])
    }

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cast: Cast) {
            Picasso.get()
                .load(cast.profile_path)
                .placeholder(R.drawable.ic_placeholder)
                .into(itemView.cast_thumbnail)

            itemView.character_name.text = cast.character
            itemView.actor_name.text = cast.name
        }
    }

    fun setData(data: List<Cast>) {
        cast = data
        notifyDataSetChanged()
    }
}