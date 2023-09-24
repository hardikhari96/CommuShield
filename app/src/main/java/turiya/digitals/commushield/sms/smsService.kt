package turiya.digitals.commushield.sms
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

// working , but its not for background
class smsService:Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        Log.d("MyService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "Service Started")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "Service Destroyed")
    }
}