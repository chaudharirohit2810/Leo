package com.rohit2810.leo_kotlin.ui.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import com.rohit2810.leo_kotlin.utils.sha256
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

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    fun checkLogin() {
        coroutineScope.launch {
            try {
//                Timber.d("Entered checkLogin")
                val user = getUserFromCache(context) ?: throw Exception("Nothing in cache")
                Timber.d(user.toString())
                delay(750)
                repository.checkLogin(user)
                _navigateToMain.value = true
            } catch (e: Exception) {
                delay(1000)
                Timber.d(e.localizedMessage)
                _navigateToMain.value = false
            }
        }

    }

    fun doneNavigateToMain() {
        _navigateToMain.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}