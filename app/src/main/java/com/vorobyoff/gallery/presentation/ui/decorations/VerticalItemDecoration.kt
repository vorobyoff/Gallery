package com.vorobyoff.gallery.presentation.ui.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalItemDecoration(private val top: Int = 0, private val bottom: Int = 0) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Unit = when {
        parent.getChildAdapterPosition(view) == 0 -> outRect.bottom = bottom
        parent.getChildAdapterPosition(view) == parent.childCount -> outRect.top = top
        else -> {
            outRect.bottom = bottom
            outRect.top = top
        }
    }
}