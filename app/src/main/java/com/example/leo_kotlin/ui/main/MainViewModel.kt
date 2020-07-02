package com.example.leo_kotlin.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leo_kotlin.repository.TroubleRepository
import com.example.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(
    private val context: Context
): ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = TroubleRepository.getInstance(context)


    // Util for navigating to maps
    private val _navigateToMaps = MutableLiveData<Boolean>()
    val navigateToMaps : LiveData<Boolean>
        get() = _navigateToMaps

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToEmergencyContacts = MutableLiveData<Boolean>()
    val navigateToEmergencyContacts: LiveData<Boolean>
        get() = _navigateToEmergencyContacts

    private val _snackbarMsg = MutableLiveData<String>()
    val snackbarMsg: LiveData<String>
        get() = _snackbarMsg

    fun goToEmergencyContacts() {
        _navigateToEmergencyContacts.value = true
    }

    fun doneGoToEmergencyContacts() {
        _navigateToEmergencyContacts.value = null
    }

    // To navigate to map
    fun navigateToMaps() {
        _navigateToMaps.value = true
    }

    // After navigated to map
    fun doneNavigateToMaps() {
        _navigateToMaps.value = null
    }


    private val _sendTrouble = MutableLiveData<Boolean>()
    val sendTrouble: LiveData<Boolean>
        get() = _sendTrouble

    fun sendTrouble() {
        _sendTrouble.value = true
    }

    fun doneSendTrouble() {
        _sendTrouble.value = null
    }

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg : LiveData<String>
        get() = _toastMsg

    fun markTrouble() {
        coroutineScope.launch {
            try {
                val user = getUserFromCache(context) ?: throw Exception("Some error occured")
                repository.markTrouble(user)
                doneSendTrouble()
            }catch (e: Exception) {
                _toastMsg.value = e.localizedMessage
            }

        }
    }

    fun logoutUser() {
        coroutineScope.launch {
            try {
                val name = getUserFromCache(context)?.name
                repository.logoutUser()
                _snackbarMsg.value = "See you soon ${name}!!"
                _navigateToLogin.value = true
            }catch (e: Exception) {
                _toastMsg.value = e.localizedMessage
            }

        }
    }

    fun doneNavigateToLogin() {
        _navigateToLogin.value = null
    }

    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    fun doneShowingSnackbarMsg() {
        _snackbarMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}