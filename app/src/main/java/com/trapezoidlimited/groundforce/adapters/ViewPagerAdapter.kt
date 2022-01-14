package com.trapezoidlimited.groundforce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.adapters.mission.OnMissionItemClickListener
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.mission.QuestionItem
import kotlinx.android.synthetic.main.fragment_single_survey_question.view.*
import kotlinx.android.synthetic.main.mission_item.view.*

class ViewPagerAdapter(
    var questionItem: MutableList<QuestionItem>,
    val clickListener: OnQuestionItemClickListener
): RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionTextView: TextView = itemView.survey_questions_quest_title_tv
        private val optionOneButton: Button = itemView.survey_questions_option_one_btn
        private val optionTwoButton: Button = itemView.survey_questions_option_two_btn
        private val optionThreeButton: Button = itemView.survey_questions_option_three_btn

        fun initialize(question: QuestionItem, action: OnQuestionItemClickListener){
            questionTextView.text = question.question
            optionOneButton.text = question.optionOne
            optionTwoButton.text = question.optionTwo
            optionThreeButton.text = question.optionThree

            optionOneButton.setOnClickListener {
                action.onOption(question.optionOne, 1)
            }
            optionTwoButton.setOnClickListener {
                action.onOption(question.optionTwo, 2)
            }
            optionThreeButton.setOnClickListener {
                action.onOption(question.optionThree, 3)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_single_survey_question, parent, false)

        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewPagerViewHolder, position: Int) {
        holder.initialize(questionItem[position], clickListener )
    }

    override fun getItemCount(): Int {
        return questionItem.size
    }

    fun setMyList(question: MutableList<QuestionItem>) {
        this.questionItem = question
        notifyDataSetChanged()
    }

}

 interface OnQuestionItemClickListener{
     fun onOption(option: String, position: Int)
 }