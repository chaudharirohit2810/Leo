package com.rohit2810.leo_kotlin.ui.feedback

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.ui.otp.VerifyOtpViewModel
import java.lang.IllegalArgumentException

class FeedbackViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
            return FeedbackViewModel(context) as T
        }
        throw IllegalArgumentException("Not assignable to verify view model")
    }

}