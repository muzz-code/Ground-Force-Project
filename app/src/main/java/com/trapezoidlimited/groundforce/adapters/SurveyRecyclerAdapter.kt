package com.trapezoidlimited.groundforce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.SurveyData
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.mission.SurveyItem
import kotlinx.android.synthetic.main.single_survey_item.view.*

class SurveyRecyclerAdapter(
    private var surveyList: MutableList<SurveyData>,
    private var clickListener: OnSurveyClickListener
) :
    RecyclerView.Adapter<SurveyRecyclerAdapter.SurveyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val surveyView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_survey_item, parent, false)
        return SurveyViewHolder(surveyView)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.initialize(surveyList[position], clickListener)
    }

    override fun getItemCount() = surveyList.size

    class SurveyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Reference to Views
        private var surveyTitle: TextView = itemView.single_survey_item_title_tv
        private var surveyBody: TextView = itemView.single_survey_item_body_tv
        private var surveyProgressBar: ProgressBar =
            itemView.single_survey_item_title_progress_bar_tv
        private var surveyAccept: TextView = itemView.single_survey_item_title_accept_tv
        private var surveyDecline: TextView = itemView.single_survey_item_title_decline_tv


        fun initialize(item: SurveyData, action: OnSurveyClickListener) {
            surveyBody.text = item.body
            surveyTitle.text = item.title
            surveyProgressBar.progress = item.progress

            //Accept Survey Listener
            surveyAccept.setOnClickListener {
                item.id?.let { it1 -> action.onAcceptClick(item, adapterPosition, it1) }
            }

            //Decline Survey
            surveyDecline.setOnClickListener {
                item.id?.let { it1 -> action.onDeleteClick(item, adapterPosition, it1) }
            }
        }
    }

    fun setMyList(survey: MutableList<SurveyData>) {
        this.surveyList = survey
        notifyDataSetChanged()
    }

    //OnClick Listener Interface
    interface OnSurveyClickListener {
        fun onAcceptClick(survey: SurveyData, position: Int, id: String)
        fun onDeleteClick(survey: SurveyData, position: Int, id: String)
    }

}