package com.rohit2810.leo_kotlin.ui.otp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class VerifyOtpViewModel(var context: Context, var user: User) : ViewModel() {
    val otp = MutableLiveData<String>()

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg : LiveData<String>
        get() = _toastMsg

    private val _registeredUser = MutableLiveData<User>()
    val registeredUser: LiveData<User>
        get() = _registeredUser

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible

    private val _navigateToNext = MutableLiveData<Boolean>()
    val navigateToNext : LiveData<Boolean>
        get() = _navigateToNext

    init {
        _isProgressBarVisible.value = false
    }

    private var job = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private var repository = TroubleRepository.getInstance(context)

    fun verifyOTP() {
        coroutineScope.launch {
            try {
                if(otp.value.isNullOrEmpty()) throw Exception("Please enter OTP")
                _isProgressBarVisible.value = true
                user.otp = otp.value
                repository.verifyOtp(otp.value!!.toInt(), user)
                _registeredUser.value = user
                _navigateToNext.value = true
                _isProgressBarVisible.value = false
            }catch (e: Exception) {
                _toastMsg.value = e.localizedMessage
                _isProgressBarVisible.value = false
            }
        }
    }

    fun resendOTP() {
        coroutineScope.launch {
            try {
                repository.sendOtp(user)
            }catch (e: Exception) {
                _toastMsg.value = e.localizedMessage
            }
        }
    }

    fun doneNavigateToEmergencyContact() {
        _registeredUser.value = null
    }

    fun doneNavigateToNext() {
        _navigateToNext.value = null
    }

    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}