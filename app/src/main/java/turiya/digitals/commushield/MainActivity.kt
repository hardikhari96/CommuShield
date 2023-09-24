package turiya.digitals.commushield

import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_SMS
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.Telephony
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import turiya.digitals.commushield.sms.smsService
import java.util.*


class MainActivity : AppCompatActivity() {
    private val permissions =  ArrayList<String>()
    private var permissionsToRequest = ArrayList<String>()
    private val ALL_PERMISSIONS_RESULT = 107
    var allPermissionsGranted = true
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("error_one ","Test")
        permissions.add(READ_SMS)
        permissions.add(READ_CALL_LOG)
        permissionsToRequest = findUnAskedPermissions(permissions)
        if(permissionsToRequest.size  != 0){
            requestPermissions(
                (permissionsToRequest.toTypedArray<String?>()),
                ALL_PERMISSIONS_RESULT
            )
        }
        startService(Intent(this, smsService::class.java))
//        getSMS()
//        getCallLogs()
    }

    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }
    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }
    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }
    private fun getSMS() {
        val inboxURI = Uri.parse("content://sms/inbox")
        val reqCols = arrayOf(
            Telephony.Sms.Inbox._ID,
            Telephony.Sms.Inbox.ADDRESS,
            Telephony.Sms.Inbox.BODY,
            Telephony.Sms.Inbox.DATE
        )
        val cr = contentResolver
        cr.query(inboxURI, reqCols, null, null, null)?.use { c ->
            if (c.moveToFirst()) {
                val senderColumnIndex = c.getColumnIndexOrThrow(Telephony.Sms.Inbox.ADDRESS)
                val messageColumnIndex = c.getColumnIndexOrThrow(Telephony.Sms.Inbox.BODY)
                val dateColumnIndex = c.getColumnIndexOrThrow(Telephony.Sms.Inbox.DATE)
                do {
                    val sender = c.getString(senderColumnIndex)
                    val message = c.getString(messageColumnIndex)
                    val date = c.getString(dateColumnIndex)

                    // Log the message details
                    //Log.d("SmsReader", "Sender: $sender, Message: $message, Date: $date")
                } while (c.moveToNext())
            }
        }
    }

    private fun getCallLogs() {
        val reqCols = arrayOf(CallLog.Calls._ID, CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION,        CallLog.Calls.VOICEMAIL_URI, // Additional field
            CallLog.Calls.FEATURES,CallLog.Calls.VOICEMAIL_URI)
        contentResolver.query(CallLog.Calls.CONTENT_URI, reqCols, null, null, null)?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            Log.d("Harikrushna", "index: $typeIndex")
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val voicemailIndex = cursor.getColumnIndex(CallLog.Calls.VOICEMAIL_URI) // Additional field
            val featuresIndex = cursor.getColumnIndex(CallLog.Calls.FEATURES) // Additional field

            while (cursor.moveToNext()) {


                val phNumber = cursor.getString(numberIndex)
                val callType = cursor.getInt(typeIndex)
                Log.d("Harikrushna", "Raw Call Type: $callType")
                val callDate = cursor.getLong(dateIndex)
                val callDayTime = Date(callDate)
                val callDuration = cursor.getLong(durationIndex)
                val voicemailUri = cursor.getString(voicemailIndex) // Additional field
                val features = cursor.getInt(featuresIndex) // Additional field

                var dir: String? = null
                when (callType) {
                    CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
                    CallLog.Calls.REJECTED_TYPE -> dir = "REJECTED"
                    CallLog.Calls.BLOCKED_TYPE -> dir = "BLOCKED"
                    CallLog.Calls.VOICEMAIL_TYPE -> dir = "VOICEMAIL"
                    CallLog.Calls.ANSWERED_EXTERNALLY_TYPE -> dir = "ANSWERED_EXTERNALLY"
                    else -> dir = "UNKNOWN"
                }
                val isWifiCalling = features and CallLog.Calls.FEATURES_WIFI != 0

                Log.d("Harikrushna",
                    """
                Phone Number: $phNumber
                Call Type: $dir
                Call Date: $callDayTime
                Call duration in sec : $callDuration
                Voicemail URI: $voicemailUri
                Features: $features
                Is WiFi Calling: $isWifiCalling
                """
                )
            }
        }
    }

}