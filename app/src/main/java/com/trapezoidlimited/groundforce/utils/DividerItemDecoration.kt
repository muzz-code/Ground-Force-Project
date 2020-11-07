package com.trapezoidlimited.groundforce.utils

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.adapters.NotificationsAdapter

class DividerItemDecoration(color: Int, private val heightInPixels: Int) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + heightInPixels
            val position = parent.getChildAdapterPosition(child)
            val viewType = parent.adapter?.getItemViewType(position)

            if (viewType == NotificationsAdapter.ViewType.NOTIFICATIONS.ordinal) {
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

            }
        }
    }

}