package com.rohit2810.leo_kotlin.services.wifidirect

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.*
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import com.rohit2810.leo_kotlin.Application
import com.rohit2810.leo_kotlin.Application.Companion.channel
import com.rohit2810.leo_kotlin.Application.Companion.connectionInfoListener
import com.rohit2810.leo_kotlin.Application.Companion.isConnected
import com.rohit2810.leo_kotlin.Application.Companion.manager
import com.rohit2810.leo_kotlin.Application.Companion.peerListListener
import com.rohit2810.leo_kotlin.Application.Companion.peers
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.models.User
import com.rohit2810.leo_kotlin.receivers.WifiP2PReceiver
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import java.lang.Runnable
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

val MESSAGE_READ = 1

class WiFiP2PServiceLeo() : Service() {

    private lateinit var wifiP2PReceiver: WifiP2PReceiver
    private lateinit var serverClass: ServerClass
    private lateinit var clientClass: ClientClass
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    var port = 9029
//    private var serverSocket: Socket? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        Timber.d("Wifi p2p service stared")
        var intent1 = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0)


        var intentFilter = IntentFilter()
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        peerListListener = object : WifiP2pManager.PeerListListener {
            override fun onPeersAvailable(peerList: WifiP2pDeviceList?) {
                Timber.d("Listening to peers")
                peerList?.let { it ->
                    Timber.d(it.toString())
                    val refreshedPeers = it.deviceList
                    if (refreshedPeers != peers) {
                        peers.clear()
                        peers.addAll(refreshedPeers)
                        Timber.d(Application.peers.toString())
                    }
                    if (peers.isEmpty()) {
                        Timber.d("No Devices found")
                        return
                    }
                }
            }
        }

        connectionInfoListener = object : WifiP2pManager.ConnectionInfoListener {
            override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
                Timber.d("Connection Info is available")
                info?.let { it ->
                    val inetAddress = it.groupOwnerAddress
//                    port += 1
                    if (it.groupFormed && it.isGroupOwner) {
                        Timber.d("Server")
                        serverClass = ServerClass()
                        serverClass!!.start()
                    } else if (it.groupFormed) {
                        Timber.d("Client")
                        val socket = Socket()
                        clientClass =
                            ClientClass(hostAdd = inetAddress.hostAddress, socket = socket)
                        Thread.sleep(1000)
                        clientClass!!.start()
                    }
                }
            }

        }

        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager!!.initialize(this, mainLooper, null)
        wifiP2PReceiver =
            WifiP2PReceiver(manager!!, peerListListener!!, channel!!, connectionInfoListener!!)


        registerReceiver(wifiP2PReceiver, intentFilter)
        discoverPeers()


        startForeground(1, foregroundNotification(this, pendingIntent))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wifiP2PReceiver)
    }

    fun discoverPeers() {
        //Initializing peer discovery
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Timber.d("Initialization Successful")
                manager?.requestPeers(channel, peerListListener)
            }

            override fun onFailure(reason: Int) {
                Timber.d("Something went wrong ${reason}")
                coroutineScope.launch {
                    delay(3 * 1000)
                    discoverPeers()
                }
            }
        })
    }

    var handler = Handler(object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                MESSAGE_READ -> {
                    var readBuff = msg.obj as ByteArray
                    var newMsg = String(readBuff, 0, msg.arg1)
                    Timber.d(newMsg)
                    this@WiFiP2PServiceLeo.showToast("Message Received")
                    coroutineScope.launch {
                        TroubleRepository.getInstance(this@WiFiP2PServiceLeo).markTroubleP2P(
                            stringToTrouble(newMsg)!!
                        )
                    }
                }
                else -> {
                    Timber.d("No message found")
                }
            }
            return true
        }

    })


    inner class ClientClass(val hostAdd: String, val socket: Socket) : Thread() {
        override fun run() {
            try {
                Thread.sleep(1000)
//                if (!clientSocket?.isConnected!!) {
                Timber.d("Socket is not connected")
                socket.connect(InetSocketAddress(hostAdd, port), 1000)
//                }
                Timber.d("Inside client class connected")
                var sendReceive = SendReceive(socket)
                sendReceive.start()
                var msg = "Rohit Chaudhari"
                var userString = getUserFromCache(this@WiFiP2PServiceLeo).toString()
                var user = getUserFromCache(this@WiFiP2PServiceLeo)
                var userMsg =
                    user?.username + "\n" + getLatitudeFromCache(this@WiFiP2PServiceLeo) + "\n" + getLongitudeFromCache(
                        this@WiFiP2PServiceLeo
                    ) + "\n"
                for (contact in user?.emergencyContacts!!) {
                    if (!contact.isNullOrEmpty()) {
                        userMsg += contact
                        userMsg += "\n"
                    }
                }
                Timber.d("Sending message to server")
                sendReceive.write(userMsg.toByteArray())

            } catch (e: Exception) {
                Timber.d(e)
                Timber.d(e.localizedMessage)
            }
            try {
                socket.close()
            } catch (e: Exception) {
                Timber.d(e)
                Timber.d(e.localizedMessage)
            }


        }
    }

    inner class ServerClass : Thread() {
        override fun run() {
            try {
                var serverSocket = ServerSocket(port)
                var socket = serverSocket.accept()
                Timber.d("Accepted by server now receiving data")
                var sendReceive = SendReceive(socket)
                sendReceive.start()
                sendReceive.read()
                serverSocket.close()
            } catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }
        }
    }

    inner class SendReceive(val socket: Socket) : Thread() {
        private lateinit var inputStream: InputStream
        private lateinit var outputStream: OutputStream

        init {
            try {
                inputStream = socket.getInputStream()
                outputStream = socket.getOutputStream()
            } catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }

        }

        fun read() {
            try {
                var buffer = ByteArray(2048)
                var bytes = inputStream.read(buffer)
                if (bytes > 0) {
                    handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget()
                    socket.close()
                }
            } catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }
        }

        fun write(byteArray: ByteArray) {
            try {
                outputStream.write(byteArray)
            } catch (e: Exception) {
                Timber.d(e.localizedMessage)
            }
        }
    }

}




