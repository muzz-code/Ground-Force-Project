package com.trapezoidlimited.groundforce.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.model.NotificationItem
import com.trapezoidlimited.groundforce.model.NotificationsItem
import kotlinx.android.synthetic.main.notification_header_recycler_item.view.*
import kotlinx.android.synthetic.main.notification_item_recycler_item.view.*

class NotificationsAdapter(private val notificationsItem: MutableList<NotificationsItem>):
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var notificationsGotten: NotificationItem


         fun bind(notification: NotificationsItem){
             if (notification.isHeader){
                 itemView.notification_header_recycler_item_title_tv.text = notification.notificationsHeader.status
             } else{
                 this.notificationsGotten = notification.notificationItem
                 val context = itemView.context
                 itemView.notifications_item_recycler_item_image_img.setImageResource(notificationsGotten.image)
                 itemView.notifications_item_recycler_item_message_tv.text = notificationsGotten.message
                 itemView.notifications_item_recycler_item_date_tv.text = notificationsGotten.date
             }


             if(notification.isHeader){
                 if (notification.notificationsHeader.status == "Older Notifications"){
                     var s = itemView.resources.getColor(R.color.colorWhite)
                     itemView.notification_header_recycler_item_root_layout.setBackgroundColor(s)

                 }
             } else{
                 if (notificationsGotten.isNew == "Older Notifications"){
                     var s = itemView.resources.getColor(R.color.colorWhite)
                     itemView.notification_item_recycler_item_root_layout.setBackgroundColor(s)
                 }
             }

             Log.d("CHECKINGS", "$notification")
         }

        override fun onClick(p0: View?) {
        }

    }

    enum class ViewType{
        HEADER,
        NOTIFICATIONS

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val HeaderView = LayoutInflater.from(parent.context).inflate(R.layout.notification_header_recycler_item, parent, false)
        val NotificationsView = LayoutInflater.from(parent.context).inflate(R.layout.notification_item_recycler_item, parent, false)

        return when(viewType) {
            ViewType.HEADER.ordinal -> ViewHolder(HeaderView)
            ViewType.NOTIFICATIONS.ordinal -> ViewHolder(NotificationsView)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("CHECKINGSCALLED", "CALLED")
       holder.bind(notificationsItem[position])
    }

    override fun getItemCount() = notificationsItem.size

    override fun getItemViewType(position: Int): Int {
        return if (notificationsItem[position].isHeader){
            ViewType.HEADER.ordinal
        } else{
            ViewType.NOTIFICATIONS.ordinal
        }
    }


    fun updateNotifications(notification: List<NotificationsItem>){
        Log.d("CHECKINGSTWO", "${notification.toString()}")
        this.notificationsItem.clear()
        this.notificationsItem.addAll(notification)
        notifyDataSetChanged()
    }
}




