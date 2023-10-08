package turiya.digitals.commushield.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

class SocketService : Service() {
    companion object {
        private lateinit var mSocket: Socket
        fun getSocket(): Socket {
            return mSocket
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MySocketService", "Service Created")
        // Initialize and connect the socket
        val opts = IO.Options()
        opts.timeout = 10000 // 10 seconds
        opts.reconnection = true
        mSocket = IO.socket("https://covishield-turiyadigitals-2ba6c08eb6e2.herokuapp.com",opts) // Replace with your server URL
        mSocket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketIO", "Connected")
        }.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketIO", "Disconnected")
        }.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("SocketIO", "Connection Error: ${it[0]}")
        }
        mSocket.connect()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Here you can send a message to the server in the background
        return START_STICKY
    }

    private fun sendMessage(message: JSONObject) {
        mSocket.emit("clientMessage", message)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
    }
}
