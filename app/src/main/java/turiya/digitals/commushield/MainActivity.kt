package turiya.digitals.commushield

import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_SMS
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val permissions =  ArrayList<String>()
    private var permissionsToRequest = ArrayList<String>();
    private val ALL_PERMISSIONS_RESULT = 107;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("error_one ","Test");
        permissions.add(READ_SMS)
        permissions.add(READ_CALL_LOG)
        permissionsToRequest = findUnAskedPermissions(permissions)
        if(permissionsToRequest.size  != 0){
            requestPermissions(
                (permissionsToRequest.toTypedArray<String?>())!!,
                ALL_PERMISSIONS_RESULT
            )
        }

        getSMS()
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
    private fun getSMS(){
        val inboxURI = Uri.parse("content://sms/inbox")
        val reqCols = arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE)
        val cr = contentResolver
        val c = cr.query(inboxURI, reqCols, null, null, null)
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    val senderColumnIndex = c.getColumnIndexOrThrow(Telephony.Sms.Inbox.ADDRESS)
                    val messageColumnIndex = c.getColumnIndexOrThrow(Telephony.Sms.Inbox.BODY)
                    val dateColumnIndex = c.getColumnIndexOrThrow(Telephony.Sms.Inbox.DATE)
                    do {
                        val sender = c.getString(senderColumnIndex)
                        val message = c.getString(messageColumnIndex)
                        val date = c.getString(dateColumnIndex)

                        // Log the message details
                        Log.d("SmsReader", "Sender: $sender, Message: $message, Date: $date")
                    } while (c.moveToNext())
                }
            } finally {
                c.close() // Close the cursor when you're done with it
            }
        }
    }
}