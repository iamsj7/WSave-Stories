package com.nerdinfusions.wsave.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nerdinfusions.wsave.R

class RecyclerFormatter {

    class SimpleDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
        private val mDivider: Drawable? = ContextCompat.getDrawable(context, R.drawable.simple_recycler_divider)

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider!!.intrinsicHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    class DoubleDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
        private val mDivider: Drawable? = ContextCompat.getDrawable(context, R.drawable.recycler_divider)

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + mDivider!!.intrinsicHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    class GridItemDecoration(private val context: Context, private val spanCount: Int, private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            //super.getItemOffsets(outRect, view, parent, state)
            val pos = parent.getChildAdapterPosition(view)
            val space = Math.round(space * context.resources.displayMetrics.density)
            val column = pos % spanCount

            outRect.left = space - column * space / spanCount
            outRect.right = (column + 1) * space / spanCount

            if (pos < spanCount) {
                outRect.top = space
            }

            outRect.bottom = space
        }
    }
}
