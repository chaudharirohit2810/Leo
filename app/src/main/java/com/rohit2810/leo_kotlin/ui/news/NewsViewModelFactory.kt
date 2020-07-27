package com.rohit2810.leo_kotlin.ui.news

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rohit2810.leo_kotlin.ui.otp.VerifyOtpViewModel
import java.lang.IllegalArgumentException

class NewsViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(context) as T
        }
        throw IllegalArgumentException("Not assignable to news view model")
    }
}