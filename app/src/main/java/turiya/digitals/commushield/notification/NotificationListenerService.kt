package turiya.digitals.commushield.notification

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val notification: Notification = sbn?.notification ?: return
        val extras: Bundle = notification.extras

        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString()

        Log.d("MyNotificationListener", "Package: ${sbn.packageName}")
        Log.d("MyNotificationListener", "Title: $title")
        Log.d("MyNotificationListener", "Text: $text")
        Log.d("MyNotificationListener", "SubText: $subText")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("MyNotificationListener", "Notification removed: ${sbn?.packageName}")
    }
}
