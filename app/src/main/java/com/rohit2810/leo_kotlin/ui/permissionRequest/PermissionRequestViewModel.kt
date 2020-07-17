package com.rohit2810.leo_kotlin.ui.permissionRequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionRequestViewModel : ViewModel() {
    private val _isLocationPermissionGranted = MutableLiveData<Boolean>()
    val isLocationPermissionGranted: LiveData<Boolean>
        get() = _isLocationPermissionGranted

    private val _isSMSPermissionGranted = MutableLiveData<Boolean>()
    val isSMSPermissionGranted: LiveData<Boolean>
        get() = _isSMSPermissionGranted

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain : LiveData<Boolean>
        get() = _navigateToMain

    private val _allPermissionsGranted = MutableLiveData<Boolean>()
    val allPermissionsGranted: LiveData<Boolean>
        get() = _allPermissionsGranted




    init {
        _isLocationPermissionGranted.value = false
        _isSMSPermissionGranted.value = false
        _allPermissionsGranted.value = false
    }

    fun allPermissionsGranted() {
        _allPermissionsGranted.value = true
    }

    fun locationPermissionGranted() {
        _isLocationPermissionGranted.value = true
    }

    fun locationPermissionNotGranted() {
        _isLocationPermissionGranted.value = false
    }

    fun smsPermissionGranted() {
        _isSMSPermissionGranted.value = true
    }

    fun smsPermissionNotGranted() {
        _isSMSPermissionGranted.value = false
    }

    fun navigateToMainClick() {
        _navigateToMain.value = true
    }

    fun doneNavigateToMain() {
        _navigateToMain.value = null
    }


}