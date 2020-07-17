package com.rohit2810.leo_kotlin.ui.login

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import com.rohit2810.leo_kotlin.utils.sha256
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(private val context: Context) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository = TroubleRepository.getInstance(context)

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _isProgressVisible = MutableLiveData<Boolean>()
    val isProgressVisible: LiveData<Boolean>
        get() = _isProgressVisible

    private val _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister: LiveData<Boolean>
        get() = _navigateToRegister

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private val _snackbarMsg = MutableLiveData<String>()
    val snackbarMsg: LiveData<String>
        get() = _snackbarMsg

    init {
        _isProgressVisible.value = false
    }


    fun navigateToRegister() {
        _navigateToRegister.value = true
    }

    fun doneNavigateToRegister() {
        _navigateToRegister.value = null
    }

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun navigateToMain() {

        if(username.value == null || password.value == null) {
            _toastMsg.value = "Please fill all the fields"
            return
        }
        username.value?.let {
            password.value?.let {
                coroutineScope.launch {
                    _isProgressVisible.value = true
                    try {
//                        Timber.d(sha256(password.value!!))
                        val user = User(username.value!!, password = password.value!!)
                        Timber.d(user.username)
                        repository.loginUser(user)
                        _snackbarMsg.value = "Welcome ${getUserFromCache(context)?.name}!!"
                        _navigateToMain.value = true
                        _isProgressVisible.value = false
                    } catch (e: Exception) {
//                        Timber.d(e)
                        _snackbarMsg.value = e.localizedMessage
                        _isProgressVisible.value = false
                    }
                }
            }
        }

    }

    fun doneNavigateToMain() {
        _navigateToMain.value = null
    }

    fun doneShowingSnackBar() {
        _snackbarMsg.value = null
    }


    fun doneToastMsg() {
        _toastMsg.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}