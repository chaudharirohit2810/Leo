package com.example.leo_kotlin.ui.emergencyContacts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leo_kotlin.models.User
import com.example.leo_kotlin.repository.TroubleRepository
import com.example.leo_kotlin.utils.getEmergencyContactFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class EmergencyContactsViewModel(
    private val previousUser: User,
    private val repository: TroubleRepository,
    private val context: Context,
    private val toUpdate: Boolean
) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible


    val phone1 = MutableLiveData<String>()
    val phone2 = MutableLiveData<String>()
    val phone3 = MutableLiveData<String>()
    val phone4 = MutableLiveData<String?>()
    val phone5 = MutableLiveData<String?>()


    init {
        _isProgressBarVisible.value = false
        if (toUpdate) {
            initList()
        }

    }

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _snackbarMsg = MutableLiveData<String>()
    val snackbarMsg: LiveData<String>
        get() = _snackbarMsg


    // Register new user and then navigate to main fragment
    fun addUserAndNavigateToMain() {
        coroutineScope.launch {
            _isProgressBarVisible.value = true
            try {
                if (!phone1.value.isNullOrEmpty() || !phone2.value.isNullOrEmpty() || !phone3.value.isNullOrEmpty()) {
                    val newUser = previousUser
                    val list = mutableListOf<String?>(phone1.value, phone2.value, phone3.value, phone4.value, phone5.value)
                    newUser.emergencyContacts = list

                    // To update is to check if we are entered in activity to update the user or to user initial user
                    if (toUpdate) {
                        repository.updateUser(newUser)
                    } else {
                        Timber.d("Entered")
                        // Check repo method
                        repository.registerUser(newUser)
                    }

                    if(toUpdate) {
                        _snackbarMsg.value = "Emergency contacts updated"
                    }else {
                        _snackbarMsg.value = "Welcome ${newUser.name}"
                    }
                    _isProgressBarVisible.value = false

                    _navigateToMain.value = true

                } else {
                    _isProgressBarVisible.value = false
                    _toastMsg.value = "Please Enter at least 3 emergency contacts"
                }
            } catch (e: Exception) {
                _isProgressBarVisible.value = false
                Timber.d(e.localizedMessage)
                _toastMsg.value = e.localizedMessage
            }

        }
    }

    private fun initList() {
        val list = getEmergencyContactFromCache(context)
        list.let {
            list[0]?.let {
                phone1.value = it
            }
            list[1]?.let {
                phone2.value = it
            }
            list[2]?.let {
                phone3.value = it
            }
            list[3]?.let {
                phone4.value = it
            }
            list[4]?.let {
                phone5.value = it
            }

        }
    }


    // After the toast message is shown
    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    //After the snackbar is shown
    fun doneShowingSnackbar() {
        _snackbarMsg.value = null
    }

    fun doneNavigateToMain() {
        _navigateToMain.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}