package turiya.digitals.commushield.callLogs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING -> {
                if(incomingNumber != null) Log.d("CallReceiver", "Incoming call from: $incomingNumber")
            }
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                Log.d("CallReceiver", "Call answered")
                // Handle call answered
            }
            TelephonyManager.EXTRA_STATE_IDLE -> {
                Log.d("CallReceiver", "Call ended")
                // Handle call ended
            }
        }
    }
}
