package com.rohit2810.leo_kotlin.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.rohit2810.leo_kotlin.R

fun Context.showToast(msg: String) {
//    Toast.makeTe/xt(this, msg, Toast.LENGTH_SHORT).show()
    val toast = Toast(this)
    val view : View = LayoutInflater.from(this).inflate(R.layout.toast_dialog, null)
    val textView = view.findViewById<TextView>(R.id.toast_text)
    textView.text = msg
    toast.view = view
    toast.duration = Toast.LENGTH_SHORT
    toast.show()

}