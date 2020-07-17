package com.rohit2810.leo_kotlin.utils

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64.getEncoder

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun sha256(text: String) : String {
    val bytes = text.toString().toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}