package com.example.leo_kotlin.utils

import android.telephony.SmsManager
import timber.log.Timber
import java.lang.Exception

private val smsManager = SmsManager.getDefault()

fun sendMessage(latitude: Double, longitude: Double, contacts: List<String?>) {
    val msg ="I am in trouble. " + "\n" + "My location is : \n"
    val title = "https://www.google.com/maps/@$latitude,$longitude,15z"
    for(contact in contacts) {
        Timber.d(contact)
        contact?.let {
            try {
                smsManager.sendTextMessage(contact, null, msg + title, null, null)
            }catch (e: Exception){
                Timber.d(e.localizedMessage)
            }

        }
    }
}