package com.rohit2810.leo_kotlin.ui.otp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rohit2810.leo_kotlin.models.user.User
import java.lang.IllegalArgumentException

class VerifyOtpViewModelFactory(var context: Context, var user: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VerifyOtpViewModel::class.java)) {
            return VerifyOtpViewModel(context, user) as T
        }
        throw IllegalArgumentException("Not assignable to verify view model")
    }

}