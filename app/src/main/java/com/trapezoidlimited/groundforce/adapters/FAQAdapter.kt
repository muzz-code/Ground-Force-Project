package com.trapezoidlimited.groundforce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.model.FAQ
import kotlinx.android.synthetic.main.faq_list_group.view.*

class FAQAdapter (private val faqList: MutableList<FAQ>
): RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val faqQuestionTextView: TextView = itemView.faq_list_group_tv
        private val faqExplanationTextView: TextView = itemView.faq_list_child_tv

        val expandableLayout: LinearLayout = itemView.expandableList
        val indicatorImageView: ImageView = itemView.faq_list_group_indicator_iv
        val questionLayout: LinearLayout = itemView.faq_list_group_ll

        fun initialize(faq: FAQ) {
            faqQuestionTextView.text = faq.question
            faqExplanationTextView.text = faq.explanation

            questionLayout.setOnClickListener {
                faq.isExpanded = !faq.isExpanded
                notifyItemChanged(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.faq_list_group, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        holder.initialize(faqList[position])
        val isExpanded = faqList[position].isExpanded

        if (isExpanded == true) {
            holder.expandableLayout.visibility = View.VISIBLE
            holder.indicatorImageView.setImageResource(R.drawable.ic_arrow_up)
        } else {
            holder.expandableLayout.visibility = View.GONE
            holder.indicatorImageView.setImageResource(R.drawable.ic_arrow_down)
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}