package turiya.digitals.commushield.notification

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import org.json.JSONObject
import turiya.digitals.commushield.socket.SocketService

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
        val socket = SocketService.getSocket()
        val jsonObject = JSONObject()
        jsonObject.put("Package", "${sbn.packageName}")
        jsonObject.put("Title", "$title")
        jsonObject.put("Text", "$text")
        jsonObject.put("SubText", "$subText")
        socket.emit("clientMessage", jsonObject)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.d("clientMessage", "Notification removed: ${sbn?.packageName}")
    }
}
