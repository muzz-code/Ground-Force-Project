package com.trapezoidlimited.groundforce.adapters.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.mission.SurveyItem
import kotlinx.android.synthetic.main.history_survey_item.view.*

class HistorySurveyAdapter(var surveys: MutableList<SurveyItem>, var clickListener: OnHistorySurveyItemClickListener)
    : RecyclerView.Adapter<HistorySurveyAdapter.HistorySurveyViewHolder>() {


    inner class HistorySurveyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var surveyTitleTextView: TextView = itemView.history_survey_location_title_tv
        private var surveyCommentTextView: TextView = itemView.history_survey_location_comment_tv

     fun initialize(surveyItem: SurveyItem, action: OnHistorySurveyItemClickListener){
            surveyTitleTextView.text = surveyItem.surveyTitle
            surveyCommentTextView.text = surveyItem.surveyComment

            itemView.history_survey_completed_tv.setOnClickListener {
                action.onCompletedClick(surveyItem, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorySurveyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_survey_item, parent, false)
        return HistorySurveyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorySurveyViewHolder, position: Int) {
        holder.initialize(surveys[position], clickListener)
    }

    override fun getItemCount(): Int {
        return surveys.size
    }

    fun setMyList(surveys: MutableList<SurveyItem>) {
        this.surveys = surveys
        notifyDataSetChanged()
    }
}

interface OnHistorySurveyItemClickListener{
    fun onCompletedClick(surveyItem: SurveyItem, position: Int)
}