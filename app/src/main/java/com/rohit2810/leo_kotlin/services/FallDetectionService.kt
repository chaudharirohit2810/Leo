package com.rohit2810.leo_kotlin.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.detectFall
import com.rohit2810.leo_kotlin.utils.foregroundNotification
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.*
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.KITKAT)
class FallDetectionService : Service(), SensorEventListener {

    private lateinit var repository: TroubleRepository

    private lateinit var coroutineScope: CoroutineScope

    private lateinit var serviceJob: Job



    private var fallDetected: Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("Main Service")

        //Initialize pending intent for when user clicks on service it navigates it to app
        val intent1 = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0)

        // Initialize sensor manager
        val mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        // Initialize coroutines
        serviceJob = Job()
        coroutineScope = CoroutineScope(Dispatchers.Main + serviceJob)

        //Initialize repository
        repository = TroubleRepository.getInstance(this)

        // Start the service
        startForeground(1, foregroundNotification(this, pendingIntent))
    }



    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Timber.d("Accuracy: $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {
//        Timber.d(event.toString())
        event?.let {
            // Detect fall is function which returns boolean
            // Written in utils folder
            if(detectFall(event) && !fallDetected) {
                Timber.d("Entered fall detection")
                coroutineScope.launch {
                    try {
                        fallDetected = true
                        repository.markTrouble(getUserFromCache(this@FallDetectionService)!!)
                        fallDetected = false
                    }catch (e: Exception) {
                        Timber.d(e.localizedMessage)
                    }

                }
            }
        }
    }


}