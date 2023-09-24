package turiya.digitals.commushield.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val messageBody = smsMessage.messageBody
                val phoneNumber = smsMessage.originatingAddress
                Log.d("receiver", "Received SMS from $phoneNumber: $messageBody")
            }
        }
    }
}
