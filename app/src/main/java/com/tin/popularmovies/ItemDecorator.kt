package com.tin.popularmovies

import android.graphics.Rect
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

open class ItemDecorator(@DimenRes private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {

        outRect.apply {
            left =
                if (itemPosition == 0) 0 else parent.context.resources.getDimensionPixelSize(margin)
            right = 0
            top = 0
            bottom = 0
        }
    }
}
