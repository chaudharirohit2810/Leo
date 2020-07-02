package com.example.leo_kotlin.ui.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leo_kotlin.repository.TroubleRepository
import com.example.leo_kotlin.utils.getPermissionPreference
import com.example.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class SplashViewModel(private val context: Context) : ViewModel() {

    private var viewModelJob: Job = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = TroubleRepository.getInstance(context)

    init {
        checkLogin()
    }


    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private fun checkLogin() {
        coroutineScope.launch {
            try {
                val user = getUserFromCache(context) ?: throw Exception("Nothing in cache")
//                Timber.d(user.printUtil())
                if(!getPermissionPreference(context)) {
                    throw Exception("Permission Not granted")
                }
                delay(500)
                repository.checkLogin(user)
                _navigateToMain.value = true
            } catch (e: Exception) {
                delay(750)
                Timber.d(e.localizedMessage)
                _navigateToLogin.value = true
            }

        }

    }

    fun doneNavigateToMain() {
        _navigateToMain.value = null
    }

    fun doneNavigateToLogin() {
        _navigateToLogin.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}