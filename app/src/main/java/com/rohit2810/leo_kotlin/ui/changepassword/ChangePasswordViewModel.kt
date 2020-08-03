package com.rohit2810.leo_kotlin.ui.changepassword

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.sha256
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class ChangePasswordViewModel(var context: Context, var user: User) : ViewModel() {
    private var job = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Main + job)
    var password =  MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()
    var progressBar = MutableLiveData<Boolean>()

    var toastMsg = MutableLiveData<String>()

    init {
        progressBar.value = false
        toastMsg.value = null
    }

    var navigateToLogin = MutableLiveData<Boolean>()

    fun onSubmit() {
        coroutineScope.launch {
            try {
                if (password.value.isNullOrEmpty()) {
                    toastMsg.value = "Please enter valid password"
                    return@launch
                }
                if (!password.value.equals(confirmPassword.value)) {
                    toastMsg.value =  "Passwords do not match"
                    return@launch
                }
                progressBar.value = true
                user.password = password.value!!
                TroubleRepository.getInstance(context).forgotPassword(user)
                toastMsg.value = "Password Changed Successfully!!"
                navigateToLogin.value = true
                progressBar.value = false
            }catch (e: Exception) {
                Timber.d(e.localizedMessage)
                toastMsg.value = "Password change failed! Please try again"
                progressBar.value = false
            }
        }
    }

    fun doneToastMsg() {
        toastMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}