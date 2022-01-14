package com.trapezoidlimited.groundforce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.data.PaymentData
import kotlinx.android.synthetic.main.single_payment_history_recycler_layout.view.*

class PaymentHistoryRecyclerAdapter(
    private var paymentList: MutableList<PaymentData>,
) :
    RecyclerView.Adapter<PaymentHistoryRecyclerAdapter.PaymentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val paymentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_payment_history_recycler_layout, parent, false)
        return PaymentViewHolder(paymentView)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.initialize(paymentList[position])
    }

    override fun getItemCount() = paymentList.size


    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Reference to Views
        var paymentAmount: TextView = itemView.single_payment_history_amout_value_tv
        var paymentHaveReceived: TextView = itemView.single_payment_history_have_recieved_tv
        var paymentDate: TextView = itemView.single_payment_history_payment_date_tv



        fun initialize(item: PaymentData) {
            paymentAmount.text = item.amount.toString()
            paymentHaveReceived.text = item.haveReceived
            paymentDate.text = item.date
        }
    }

}