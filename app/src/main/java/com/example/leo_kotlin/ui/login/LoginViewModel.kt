package com.example.leo_kotlin.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leo_kotlin.models.User
import com.example.leo_kotlin.repository.TroubleRepository
import com.example.leo_kotlin.utils.getUserFromCache
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
                        val user = User(username.value!!, password.value!!)
                        Timber.d(user.username)
                        repository.loginUser(user)
                        _snackbarMsg.value = "Welcome ${getUserFromCache(context)?.name}!!"
                        _navigateToMain.value = true
                        _isProgressVisible.value = false
                    } catch (e: Exception) {
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