package com.rohit2810.leo_kotlin.ui.forgotpassword

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ForgotPasswordViewModel(var context: Context): ViewModel() {

    var username = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    private val _toastMsg = MutableLiveData<String>()
    val toastMsg : LiveData<String>
        get() = _toastMsg

    val registeredUser = MutableLiveData<User>()
    val progressBar = MutableLiveData<Boolean>()

    init {
        registeredUser.value = null
        progressBar.value = false
    }

    private var job = Job()
    private var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    fun onSubmit() {
        coroutineScope.launch {
            try {
                progressBar.value = true
                check()
                var user = User(username = username.value!!, email = email.value)
                TroubleRepository.getInstance(context).sendOtp(user)
                registeredUser.value = user
                progressBar.value = false
            }catch (e: Exception) {
                Timber.d(e.localizedMessage)
                _toastMsg.value = "Invalid username or email"
                progressBar.value = false
            }
        }
    }

    fun check() {
        if (username.value.isNullOrEmpty() || email.value.isNullOrEmpty()) {
            throw Exception("Please enter valid username and email")
        }
    }

    fun doneToastMsg() {
        _toastMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}