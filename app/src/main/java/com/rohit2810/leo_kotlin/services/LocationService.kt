package com.rohit2810.leo_kotlin.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.utils.addLocationinCache
import com.rohit2810.leo_kotlin.utils.foregroundNotification
import com.google.android.gms.location.*
import com.rohit2810.leo_kotlin.receivers.PowerButtonReceiver
import com.rohit2810.leo_kotlin.receivers.WifiP2PReceiver
import timber.log.Timber
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.KITKAT)
class LocationService : Service() {

    // Main class to receive location updates
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // Location request defines how often you require location updates
    private lateinit var locationRequest: LocationRequest

    //Called when new location is available
    private lateinit var locationCallback: LocationCallback

    // Variable to store current location
    private var currentLocation: Location? = null

    private lateinit var powerButtonReceiver: PowerButtonReceiver

    private lateinit var powerButtonFilter: IntentFilter




    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Timber.d("Location service started")

        //Initialize pending intent for when user clicks on service it navigates it to app
        val intent1 = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0)

        // Get location provider client
        // Get location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize location request
        locationRequest = LocationRequest().apply {
            // Different parameters for location requests
            interval = TimeUnit.SECONDS.toMillis(120)
            fastestInterval = TimeUnit.SECONDS.toMillis(60)
            maxWaitTime = TimeUnit.SECONDS.toMillis(4)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        //Use callback to get location
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                Timber.d("Location callback")
                if(locationResult?.lastLocation != null) {
                    //Get current location
                    currentLocation = locationResult.lastLocation

                    Timber.d("Latitude: ${currentLocation?.latitude} Longitude: ${currentLocation?.longitude}")

                    addLocationinCache(this@LocationService, currentLocation!!)

                    //Notify activity that new location is available
                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                    intent.putExtra(EXTRA_LOCATION, currentLocation)
//                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }else {
                    Timber.d("Location not available")
                }
            }
        }

        subscribeToLocationUpdates()

        //Setting up power Receiver
        powerButtonFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        powerButtonFilter.addAction(Intent.ACTION_SCREEN_OFF)
        powerButtonReceiver = PowerButtonReceiver()
        registerReceiver(powerButtonReceiver, powerButtonFilter)

        startForeground(1, foregroundNotification(this, pendingIntent))



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun subscribeToLocationUpdates() {
        Timber.d("Subscribe to location updates")
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }catch (e: SecurityException) {
            Timber.d(e.localizedMessage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerButtonReceiver)
        unsubscribeToLocationUpdates()
    }

    fun unsubscribeToLocationUpdates() {

        try {
            val remoteTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            remoteTask.addOnCompleteListener{task ->
                if(task.isSuccessful) {
                    stopSelf()
                }else {
                    Timber.d("Task failed")
                }
            }

        } catch (unlikely: SecurityException) {
//                SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            Timber.d("Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    companion object{
        private const val PACKAGE_NAME = "com.example.leo_kotlin"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
    }



}