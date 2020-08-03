package com.rohit2810.leo_kotlin.ui.changepassword

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rohit2810.leo_kotlin.models.user.User

class ChangePasswordViewModelFactory(var context: Context, var user: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(context, user) as T
        }
        throw Exception("Not possible")
    }

}