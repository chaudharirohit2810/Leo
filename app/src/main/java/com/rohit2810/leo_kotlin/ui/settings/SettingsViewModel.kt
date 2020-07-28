package com.rohit2810.leo_kotlin.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingsViewModel(
    val context: Context
) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = TroubleRepository.getInstance(context)

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToEmergencyContacts = MutableLiveData<Boolean>()
    val navigateToEmergencyContacts: LiveData<Boolean>
        get() = _navigateToEmergencyContacts

    private val _goToAboutUs = MutableLiveData<Boolean>()
    val goToAboutUs: LiveData<Boolean>
        get() = _goToAboutUs

    private val _toggleFallDetection = MutableLiveData<Boolean>()
    val toggleFallDetection: LiveData<Boolean>
        get() = _toggleFallDetection

    fun goToEmergencyContacts() {
        _navigateToEmergencyContacts.value = true
    }

    fun doneGoToEmergencyContacts() {
        _navigateToEmergencyContacts.value = null
    }

    fun goToAboutUs() {
        _goToAboutUs.value = true
    }


    private val _snackbarMsg = MutableLiveData<String>()
    val snackbarMsg: LiveData<String>
        get() = _snackbarMsg

    fun logoutUser() {
        coroutineScope.launch {
            try {
                val name = getUserFromCache(context)?.name
                repository.logoutUser()
                _snackbarMsg.value = "See you soon ${name}!!"
                _navigateToLogin.value = true
            }catch (e: Exception) {
                _snackbarMsg.value = e.localizedMessage
            }

        }
    }

    fun doneGoToAboutUs() {
        _goToAboutUs.value = null
    }

    fun doneShowingSnackbarMsg() {
        _snackbarMsg.value = null
    }

    fun doneNavigateToLogin() {
        _navigateToLogin.value = null
    }

    fun toggleFallDetection() {
        _toggleFallDetection.value = true
    }

    fun doneToggleFallDetection() {
        _toggleFallDetection.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}