package com.rohit2810.leo_kotlin.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import timber.log.Timber
import java.text.DecimalFormat

//var fallDetected = false

fun detectFall(event: SensorEvent): Boolean {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
        val loX: Double = event.values.get(0)!!.toDouble()
        val loY: Double = event.values.get(1)!!.toDouble()
        val loZ: Double = event.values.get(2)!!.toDouble()

        val loAccelerationReader = Math.sqrt(
            Math.pow(loX, 2.0)
                    + Math.pow(loY, 2.0)
                    + Math.pow(loZ, 2.0)
        )

        val precision = DecimalFormat("0.00")
        val ldAccRound = precision.format(loAccelerationReader).toDouble()

        if (ldAccRound > 0.2 && ldAccRound < 0.4) {
            Timber.d("Main Accuracy: $ldAccRound")
        }

        // Fall Detected
        if (ldAccRound > 0.2 && ldAccRound < 0.3) {
            Timber.d("Main Accuracy: $ldAccRound")
            return true;
        }
    }
    return false;
}