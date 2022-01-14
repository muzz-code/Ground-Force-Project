package com.trapezoidlimited.groundforce.adapters.mission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.model.mission.OngoingItem
import kotlinx.android.synthetic.main.ongoing_item.view.*

class OngoingAdapter(var ongoings: MutableList<OngoingItem>, var clickListener: OngoingItemClickListener ):
    RecyclerView.Adapter<OngoingAdapter.OngoingViewHolder>() {


    inner class OngoingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.ongoing_location_title_tv
        private val subtitleTextView: TextView = itemView.ongoing_location_subtitle_tv

        fun initialize(ongoing: OngoingItem, action: OngoingItemClickListener) {
            titleTextView.text = ongoing.locationTitle
            subtitleTextView.text = ongoing.locationSubTitle

            /** setting the onclick listener to verify text of every ongoing item on the recyclerview list */

            itemView.ongoing_verify_tv.setOnClickListener {
                ongoing.id?.let { it1 -> action.onVerifyBtnClick(ongoing, adapterPosition, it1) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OngoingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_item, parent, false)
        return OngoingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OngoingViewHolder, position: Int) {
        holder.initialize(ongoings[position], clickListener)
    }


    override fun getItemCount(): Int {
        return ongoings.size
    }

    fun setMyList(ongoings: MutableList<OngoingItem>) {
        this.ongoings = ongoings
        notifyDataSetChanged()
    }



}

interface OngoingItemClickListener{
    fun onVerifyBtnClick(ongoing: OngoingItem, position: Int, id: String)
}