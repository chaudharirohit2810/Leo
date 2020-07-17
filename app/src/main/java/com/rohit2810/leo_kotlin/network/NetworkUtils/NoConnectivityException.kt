package com.rohit2810.leo_kotlin.NetworkUtils

import java.io.IOException

class NoConnectivityException : IOException() {
    override fun getLocalizedMessage(): String? {
        return "No Internet Connection"
    }
}