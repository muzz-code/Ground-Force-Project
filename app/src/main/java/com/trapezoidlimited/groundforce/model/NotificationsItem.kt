package com.trapezoidlimited.groundforce.model

class NotificationsItem {

   lateinit var notificationItem: NotificationItem
      private set

    lateinit var notificationsHeader: NotificationsHeader
    private set

    var isHeader = false
      private set


    companion object{

        fun withMessage(notificationItem: NotificationItem): NotificationsItem{
            val notifications = NotificationsItem()
            notifications.notificationItem = notificationItem

            return notifications
        }


        fun withHeader(header: NotificationsHeader): NotificationsItem{
            val notifications =  NotificationsItem()
            notifications.notificationsHeader = header
            notifications.isHeader = true

            return notifications
        }
    }

    override fun toString(): String {
        return if (isHeader) notificationsHeader.status else notificationItem.message
    }
}