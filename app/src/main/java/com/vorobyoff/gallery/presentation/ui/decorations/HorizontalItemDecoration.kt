package com.vorobyoff.gallery.presentation.ui.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(private val left: Int = 0, private val right: Int = 0) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Unit = when {
        parent.getChildAdapterPosition(view) == 0 -> outRect.right = right
        parent.getChildAdapterPosition(view) == parent.childCount -> outRect.left = left
        else -> {
            outRect.right = right
            outRect.left = left
        }
    }
}