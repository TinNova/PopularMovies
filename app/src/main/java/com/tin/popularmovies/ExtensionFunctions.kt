package com.tin.popularmovies

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

/*
 * Helper for loading images with Picasso
 */
// TODO: Create another one that makes the ImageView GONE, overload the method
fun ImageView.loadWithPicasso(imageUrl: String, fallbackDrawableId: Int) =
    if (imageUrl.isEmpty()) Picasso.get()
        .load(fallbackDrawableId)
        .into(this)
    else Picasso.get()
        .load(imageUrl)
        .placeholder(fallbackDrawableId)
        .error(fallbackDrawableId)
        .into(this)

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

