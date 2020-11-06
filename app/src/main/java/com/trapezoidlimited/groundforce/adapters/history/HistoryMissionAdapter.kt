package com.trapezoidlimited.groundforce.adapters.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import kotlinx.android.synthetic.main.history_mission_item.view.*

class HistoryMissionAdapter(var missions: MutableList<MissionItem>,
                            var clicklistener: OnHistoryItemClickListener): RecyclerView.Adapter<HistoryMissionAdapter.HistoryMissViewHolder>() {

    inner class HistoryMissViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cityLocationTextView: TextView = itemView.history_mission_location_title_tv
        private val streetLocationTextView: TextView = itemView.history_mission_location_subtitle_tv
        private val dateTextView: TextView = itemView.history_mission_date_tv

        fun initialize(missionItem: MissionItem, action: OnHistoryItemClickListener){
            cityLocationTextView.text = missionItem.locationTitle
            streetLocationTextView.text = missionItem.locationSubTitle
            dateTextView.text = missionItem.date

            itemView.history_mission_verify_tv.setOnClickListener {
                action.onVerifyClick(missionItem, adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryMissViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.history_mission_item, parent, false)

        return HistoryMissViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryMissViewHolder, position: Int) {
        holder.initialize(missions[position], clicklistener)
    }

    override fun getItemCount(): Int {
        return missions.size
    }

    fun setMyList(missions: MutableList<MissionItem>) {
        this.missions = missions
        notifyDataSetChanged()
    }
}

interface OnHistoryItemClickListener{
    fun onVerifyClick(missionItem: MissionItem, position: Int)
}

