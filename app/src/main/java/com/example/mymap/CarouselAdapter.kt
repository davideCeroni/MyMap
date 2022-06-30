package com.example.mymap

import android.content.Context
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarouselAdapter(private val context: Context) {
    /*lateinit var items: List<Int>
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(layoutInflater.inflate(R.layout.item_rec_view_notifications, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onViewAttachedToWindow(holder: ItemViewHolder) {
        holder.onViewAppear()

        super.onViewAttachedToWindow(holder)
    }

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private lateinit var indicatorTextView: TextView

        fun bind(@DrawableRes imageId: Int) {
            /*view.findViewById<ImageView>(R.id.imgStateRecProfile).apply {
                setImageResource(imageId)
            }*/
        }

        fun onViewAppear() {
            indicatorTextView.alpha = 1.0f
        }

        /*private fun fadeAwayIndicatorTextViewWithDelay(fadeDurationInMilliSeconds: Long, delayInMilliSeconds: Long) {
            ObjectAnimator.ofFloat(indicatorTextView, "alpha", 1f, 0f).apply {
                duration = fadeDurationInMilliSeconds
                startDelay = delayInMilliSeconds
            }.start()
        }*/
    }*/
}