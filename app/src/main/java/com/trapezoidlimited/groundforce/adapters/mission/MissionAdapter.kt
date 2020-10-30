package com.trapezoidlimited.groundforce.adapters.mission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import kotlinx.android.synthetic.main.mission_item.view.*

class MissionAdapter(var missions: MutableList<MissionItem>, var clickListener: OnMissionItemClickListener)
    : RecyclerView.Adapter<MissionAdapter.MissionViewHolder>() {



    inner class MissionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.mission_location_title_tv
        private val subtitleTextView: TextView = itemView.mission_location_subtitle_tv

        fun initialize(mission: MissionItem, action: OnMissionItemClickListener) {
            titleTextView.text = mission.locationTitle
            subtitleTextView.text = mission.locationSubTitle


            /** setting the onclick listener to accept text of every mission item on the recyclerview list */

            itemView.mission_accept_tv.setOnClickListener {
                action.onAcceptClick(mission, adapterPosition)
            }

            /** setting the onclick listener to decline text of every mission item on the recyclerview list */

            itemView.mission_decline_tv.setOnClickListener {
                action.onDeclineClick(mission, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.mission_item, parent, false)

        return MissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        holder.initialize(missions[position], clickListener)
    }

    override fun getItemCount(): Int {
        return missions.size
    }

    fun setMyList(missions: MutableList<MissionItem>) {
        this.missions = missions
        notifyDataSetChanged()
    }
}

interface OnMissionItemClickListener{
    fun onAcceptClick(mission: MissionItem, position: Int)
    fun onDeclineClick(mission: MissionItem, position: Int)
}