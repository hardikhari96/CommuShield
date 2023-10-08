package turiya.digitals.commushield.socket

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketManager {
    private var socket: Socket? = null

    init {
        try {
            socket = IO.socket("http://192.168.1.22:3000")
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    fun getSocket(): Socket? {
        return socket
    }
}
