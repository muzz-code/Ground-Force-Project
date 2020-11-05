package com.trapezoidlimited.groundforce.ui.dashboard.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.databinding.FragmentNotificationsBinding
import com.trapezoidlimited.groundforce.model.NotificationItem
import com.trapezoidlimited.groundforce.model.NotificationsHeader
import com.trapezoidlimited.groundforce.model.NotificationsItem


/**
 * Notificationc Fragment
 */
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =   FragmentNotificationsBinding.inflate(inflater, container, false)




        return binding.root
    }



    fun returnNotifications(num: Int): List<NotificationsItem>{
        val notifications = mutableListOf<NotificationsItem>()
        val isNew = mutableListOf<String>("New Notification","Older Notifications")
        val notification = mutableListOf<NotificationItem>()

         val notificationOne = NotificationItem(image = R.drawable.bolt, message = "Hello Trooper Kehinde, a credit transaction of NGN100.00 just occurred in your account. Login to review. If there are any complaints or inquiries, please feel free to contact us.", date = "13 Sept, 2020 10:30 AM" , isNew = "New Notification")
        val notificationTwo = NotificationItem(image = R.drawable.bolt, message = "Hello Trooper, here's to a happy and fulfilling holiday this season. Have fun. Cheers!", date = "13 Sept, 2020 10:30 AM" , isNew = "New Notification")
        val notificationThree = NotificationItem(image = R.drawable.bolt, message = "Hello Trooper Kehinde, a credit transaction of NGN100.00 just occurred in your account. Login to review. If there are any complaints or inquiries, please feel free to contact us.", date = "13 Sept, 2020 10:30 AM" , isNew ="Older Notifications")
        val notificationFour = NotificationItem(image = R.drawable.bolt, message = "Hello Trooper, here's to a happy and fulfilling holiday this season. Have fun. Cheers!", date = "13 Sept, 2020 10:30 AM" , isNew = "Older Notifications")

        for (nu in 0..num){

            when(nu % 4){
                0 -> notification.add(notificationOne)
                1 -> notification.add(notificationTwo)
                2 -> notification.add(notificationThree)
                else -> notification.add(notificationFour)
            }
        }

        isNew.let {
            for (new in isNew){
                notifications.add(NotificationsItem.withHeader(NotificationsHeader(new)))
                val filtered = notification.filter { it.isNew  == new}.map { NotificationsItem.withMessage(it) }
                notifications.addAll(filtered)
            }
        }


        return notifications
    }
}